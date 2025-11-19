package com.project.airBnb.controller;

import com.project.airBnb.advice.ApiResponse;
import com.project.airBnb.dto.RoomDto;
import com.project.airBnb.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotel/{hotelId}/room")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
        return new ResponseEntity<>(roomService.createNewRoom(hotelId, roomDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    private ResponseEntity<List<RoomDto>> getAllRoomsByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsByHotelId(hotelId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id){
        Optional<RoomDto> hotel=roomService.getRoomById(id);
        return hotel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteRoomById(@PathVariable Long id){
        boolean deleted= roomService.deleteRoomById(id);
        return deleted ? ResponseEntity.ok(new ApiResponse<>(true)) : ResponseEntity.notFound().build();
    }
}
