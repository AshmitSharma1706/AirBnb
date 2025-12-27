package com.project.airBnb.controller;

import com.project.airBnb.dto.*;
import com.project.airBnb.service.HotelService;
import com.project.airBnb.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Search hotels", tags = {"Browse Hotels"})
    public ResponseEntity<Page<HotelPriceResponseDto>> searchHotels(@RequestBody HotelSearchRequest searchRequest){
        return ResponseEntity.ok(inventoryService.searchHotels(searchRequest));
    }

    @GetMapping("/info/{id}")
    @Operation(summary = "Get a hotel info by hotelId", tags = {"Browse Hotels"})
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long id){
        return ResponseEntity.ok(hotelService.getHotelInfoById(id));
    }
}
