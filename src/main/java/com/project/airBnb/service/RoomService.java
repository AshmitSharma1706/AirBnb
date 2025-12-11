package com.project.airBnb.service;

import com.project.airBnb.dto.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getAllRoomsByHotelId(Long hotelId);

    Optional<RoomDto> getRoomById(Long id);

    Boolean deleteRoomById(Long id);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);

}
