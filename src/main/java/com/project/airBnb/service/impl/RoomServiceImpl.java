package com.project.airBnb.service.impl;

import com.project.airBnb.dto.RoomDto;
import com.project.airBnb.entity.Hotel;
import com.project.airBnb.entity.Room;
import com.project.airBnb.entity.User;
import com.project.airBnb.exception.ResourceNotFoundException;
import com.project.airBnb.exception.UnAuthorisedException;
import com.project.airBnb.repository.HotelRepository;
import com.project.airBnb.repository.RoomRepository;
import com.project.airBnb.service.InventoryService;
import com.project.airBnb.service.RoomService;
import com.project.airBnb.util.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final HotelRepository hotelRepository;
    private  final RoomRepository roomRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating new room...");
        hotelExistById(hotelId);
        log.info("Creating new room in hotel with hotel_id: {}", hotelId);
        Hotel hotel=hotelRepository.findById(hotelId).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id "+hotelId);
        }
        Room room=modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        Room createdRoom=roomRepository.save(room);
        if (hotel.getActive()) {
            inventoryService.initializeRoomForAYear(createdRoom);
        }
        return modelMapper.map(createdRoom, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsByHotelId(Long hotelId) {
        hotelExistById(hotelId);
        log.info("Getting all room of hotel with hotel_id: {}", hotelId);
        Hotel hotel=hotelRepository.findById(hotelId).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id "+hotelId);
        }
        return hotel.getRooms().stream()
                .map((room) -> modelMapper.map(room, RoomDto.class)).toList();
    }

    @Override
    public Optional<RoomDto> getRoomById(Long id) {
        roomExistById(id);
        log.info("Finding room with id: {}", id);
        return roomRepository.findById(id)
                .map((room) -> modelMapper.map(room, RoomDto.class));
    }

    @Override
    @Transactional
    public Boolean deleteRoomById(Long id) {
        roomExistById(id);
        log.info("Deleting room with id: {}", id);
        Room room=roomRepository.findById(id).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("This user does not own this room with id "+id);
        }
        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating the room with ID: {}", roomId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = AppUtils.getCurrentUser();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        modelMapper.map(roomDto, room);
        room.setId(roomId);

//        TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDto.class);
    }

    private void roomExistById(Long id){
        boolean exist=roomRepository.existsById(id);
        if(!exist) throw new ResourceNotFoundException("Room not found with id "+id);
    }

    private void hotelExistById(Long id){
        boolean exist=hotelRepository.existsById(id);
        if(!exist) throw new ResourceNotFoundException("Hotel not found with id "+id);
    }
}
