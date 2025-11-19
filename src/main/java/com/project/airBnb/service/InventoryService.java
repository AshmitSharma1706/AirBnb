package com.project.airBnb.service;

import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.dto.HotelPriceDto;
import com.project.airBnb.dto.HotelSearchRequest;
import com.project.airBnb.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest searchRequest);
}
