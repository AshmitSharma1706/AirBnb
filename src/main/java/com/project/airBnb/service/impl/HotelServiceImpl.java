package com.project.airBnb.service.impl;

import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.dto.HotelInfoDto;
import com.project.airBnb.dto.RoomDto;
import com.project.airBnb.entity.Hotel;
import com.project.airBnb.entity.Room;
import com.project.airBnb.entity.User;
import com.project.airBnb.exception.ResourceNotFoundException;
import com.project.airBnb.exception.UnAuthorisedException;
import com.project.airBnb.repository.HotelRepository;
import com.project.airBnb.service.HotelService;
import com.project.airBnb.service.InventoryService;
import com.project.airBnb.service.RoomService;
import com.project.airBnb.util.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final RoomService roomService;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("Creating new hotel with name: {}", hotelDto.getName());
        Hotel toCreate=modelMapper.map(hotelDto, Hotel.class);
        toCreate.setActive(false);

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        toCreate.setOwner(user);

        Hotel hotel=hotelRepository.save(toCreate);
        log.info("Created a hotel with id: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Finding hotel with id: {}", id);
        hotelExistById(id);
        Hotel hotel=hotelRepository.findById(id).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id "+id);
        }
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user = AppUtils.getCurrentUser();
        log.info("Finding all hotels for the admin user with id {}", user.getId());
        List<Hotel> hotels= hotelRepository.findByOwner(user);
        return hotels.stream().
                map(hotel -> modelMapper.map(hotel, HotelDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelDto updateHotelById(Long id, Map<String, Object> updates) {
        log.info("Updating hotel with id: {}", id);
        hotelExistById(id);
        Hotel hotel=hotelRepository.findById(id).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id "+id);
        }
        updates.forEach((field,value) -> {
            Field fieldsToBeUpdate= ReflectionUtils.getRequiredField(Hotel.class,field);
            fieldsToBeUpdate.setAccessible(true);
            if(fieldsToBeUpdate.getType().isArray() && value instanceof List<?> list){
                String[] arr=list.stream().map(Object::toString).toArray(String[]::new);
                ReflectionUtils.setField(fieldsToBeUpdate, hotel, arr);
            }else {
                ReflectionUtils.setField(fieldsToBeUpdate, hotel, value);
            }
        });
        return modelMapper.map(hotelRepository.save(hotel), HotelDto.class);
    }

    @Transactional
    @Override
    public Boolean deleteHotelById(Long id) {
        log.info("Deleting hotel with id: {}", id);
        hotelExistById(id);
        Hotel hotel = hotelRepository.findById(id).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id "+id);
        }
        for(Room room:hotel.getRooms()){
            roomService.deleteRoomById(room.getId());
        }
        hotelRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public String activateHotelById(Long id) {
        log.info("Activating hotel with id: {}", id);
        hotelExistById(id);
        Hotel hotel=hotelRepository.findById(id).get();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id "+id);
        }
        if (hotel.getActive()) {
            return "Hotel with id "+id+" is already active";
        }else {
            hotel.setActive(true);
            for (Room room:hotel.getRooms()){
                inventoryService.initializeRoomForAYear(room);
            }
            hotelRepository.save(hotel);
            return "Hotel with id " + id + " is activated";
        }
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long id) {
        hotelExistById(id);
        Hotel hotel=hotelRepository.findById(id).get();
        List<RoomDto> rooms= hotel.getRooms().stream()
                .map((element) -> modelMapper.map(element, RoomDto.class)).toList();
        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }

    private void hotelExistById(Long id){
        boolean exist=hotelRepository.existsById(id);
        if(!exist) throw new ResourceNotFoundException("Hotel not found with id "+id);
    }
}
