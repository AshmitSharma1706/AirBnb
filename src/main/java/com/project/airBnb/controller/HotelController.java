package com.project.airBnb.controller;

import com.project.airBnb.advice.ApiResponse;
import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotel")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping("/create")
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto){
        return new ResponseEntity<>(hotelService.createHotel(hotelDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id){
        Optional<HotelDto> hotel=hotelService.getHotelById(id);
        return hotel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    private ResponseEntity<List<HotelDto>> getAllHotel(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        HotelDto hotel=hotelService.updateHotelById(id, updates);
        if(hotel==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hotel);
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<ApiResponse<String>> activateHotelById(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(hotelService.activateHotelById(id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteHotelById(@PathVariable Long id){
        boolean deleted= hotelService.deleteHotelById(id);
        return deleted ? ResponseEntity.ok(new ApiResponse<>(true)) : ResponseEntity.notFound().build();
    }
}
