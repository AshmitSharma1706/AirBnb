package com.project.airBnb.controller;

import com.project.airBnb.advice.ApiResponse;
import com.project.airBnb.dto.RoomDto;
import com.project.airBnb.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create a new room in a hotel", tags = {"Admin Inventory"})
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
        return new ResponseEntity<>(roomService.createNewRoom(hotelId, roomDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all rooms in a hotel", tags = {"Admin Inventory"})
    private ResponseEntity<List<RoomDto>> getAllRoomsByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsByHotelId(hotelId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a room by id", tags = {"Admin Inventory"})
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id){
        Optional<RoomDto> hotel=roomService.getRoomById(id);
        return hotel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a room by id", tags = {"Admin Inventory"})
    public ResponseEntity<ApiResponse<Boolean>> deleteRoomById(@PathVariable Long id){
        boolean deleted= roomService.deleteRoomById(id);
        return deleted ? ResponseEntity.ok(new ApiResponse<>(true)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Update a room", tags = {"Admin Inventory"})
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long hotelId, @PathVariable Long roomId,
                                                  @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId, roomId, roomDto));
    }
}
