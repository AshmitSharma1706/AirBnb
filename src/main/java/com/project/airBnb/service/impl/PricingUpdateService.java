package com.project.airBnb.service.impl;

import com.project.airBnb.entity.Hotel;
import com.project.airBnb.entity.HotelMinPrice;
import com.project.airBnb.entity.Inventory;
import com.project.airBnb.pricing_Strategies.impl.PricingService;
import com.project.airBnb.repository.HotelMinPriceRepository;
import com.project.airBnb.repository.HotelRepository;
import com.project.airBnb.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    //Scheduler to update the inventory and HotelMinPrice tables every hour

    private final HotelRepository hotelRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0 0 * * * *")
    public void updatePrice(){
        int page=0;
        int batchSize=100;
        while(true){
            Page<Hotel> hotelPage=hotelRepository.findAll(PageRequest.of(page,batchSize));
            if (hotelPage.isEmpty()){
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrices);
            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel){
        log.info("Updating hotel prices for hotel ID: {}", hotel.getId());
        LocalDate startDate=LocalDate.now();
        LocalDate endDate=startDate.plusYears(1);

        List<Inventory> inventoryList=inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);
        updateInventoryPrices(inventoryList);
        updateHotelMinPrice(hotel,inventoryList,startDate,endDate);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice=pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        //Computing minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrice=inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        //Prepare hotel price entity in bulk
        List<HotelMinPrice> prices=new ArrayList<>();
        dailyMinPrice.forEach((date, price) ->{
            HotelMinPrice hotelMinPrice=hotelMinPriceRepository.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel,date));
            hotelMinPrice.setPrice(price);
            prices.add(hotelMinPrice);
        });

        //Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(prices);
    }
}
