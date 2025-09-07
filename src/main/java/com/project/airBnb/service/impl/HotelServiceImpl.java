package com.project.airBnb.service.impl;

import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.entity.Hotel;
import com.project.airBnb.exception.ResourceNotFoundException;
import com.project.airBnb.repository.HotelRepository;
import com.project.airBnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("Creating new hotel with name: {}", hotelDto.getName());
        Hotel toCreate=modelMapper.map(hotelDto, Hotel.class);
        toCreate.setActive(false);
        Hotel hotel=hotelRepository.save(toCreate);
        log.info("Created a hotel with id: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public Optional<HotelDto> getHotelById(Long id) {
        log.info("Finding hotel with id: {}", id);
        hotelExistById(id);
        return hotelRepository.findById(id)
                .map(hotel -> modelMapper.map(hotel,HotelDto.class));
    }

    @Override
    public List<HotelDto> getAllHotels() {
        log.info("Finding all hotels...");
        List<HotelDto> hotels = new ArrayList<>();
        List<Hotel> hotelList= hotelRepository.findAll();
        return hotelList.stream().
                map(hotel -> modelMapper.map(hotel, HotelDto.class)).toList();
    }

    @Override
    public HotelDto updateHotelById(Long id, Map<String, Object> updates) {
        log.info("Updating hotel with id: {}", id);
        hotelExistById(id);
        Hotel hotel=hotelRepository.findById(id).get();
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

    @Override
    public Boolean deleteHotelById(Long id) {
        log.info("Deleting hotel with id: {}", id);
        hotelExistById(id);
        //TODO: delete the future inventories for this hotel
        hotelRepository.deleteById(id);
        return true;
    }

    @Override
    public String activateHotelById(Long id) {
        log.info("Activating hotel with id: {}", id);
        hotelExistById(id);
        Hotel hotel=hotelRepository.findById(id).get();
        if (hotel.getActive()) {
            return "Hotel with id "+id+" is already active";
        }else {
            hotel.setActive(true);
            hotelRepository.save(hotel);
            return "Hotel with id " + id + " is activated";
        }
    }

    private void hotelExistById(Long id){
        boolean exist=hotelRepository.existsById(id);
        if(!exist) throw new ResourceNotFoundException("Hotel not found with id "+id);
    }
}
