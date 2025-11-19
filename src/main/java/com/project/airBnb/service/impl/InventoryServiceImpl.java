package com.project.airBnb.service.impl;

import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.dto.HotelPriceDto;
import com.project.airBnb.dto.HotelSearchRequest;
import com.project.airBnb.entity.Hotel;
import com.project.airBnb.entity.Inventory;
import com.project.airBnb.entity.Room;
import com.project.airBnb.repository.HotelMinPriceRepository;
import com.project.airBnb.repository.InventoryRepository;
import com.project.airBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today=LocalDate.now();
        LocalDate endDate=today.plusYears(1);
        for ( ; !today.isAfter(endDate); today=today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting the inventories of room with id: {}", room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest searchRequest) {
        Pageable pageable= PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Searching hotels for {} city, from {} to {}", searchRequest.getCity(),
                searchRequest.getStartDate(), searchRequest.getEndDate());

        Long dateCount= ChronoUnit.DAYS
                .between(searchRequest.getStartDate(), searchRequest.getEndDate()) + 1;

        //Business logic - 90 days
        Page<HotelPriceDto> hotels=hotelMinPriceRepository.findHotelsWithAvailableInventory(
                searchRequest.getCity(),searchRequest.getStartDate(),searchRequest.getEndDate()
                ,searchRequest.getRoomsCount(),dateCount,pageable);
        return hotels;
    }
}
