package com.project.airBnb.controller;

import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.dto.HotelInfoDto;
import com.project.airBnb.dto.HotelPriceDto;
import com.project.airBnb.dto.HotelSearchRequest;
import com.project.airBnb.service.HotelService;
import com.project.airBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest searchRequest){
        return ResponseEntity.ok(inventoryService.searchHotels(searchRequest));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long id){
        return ResponseEntity.ok(hotelService.getHotelInfoById(id));
    }
}
