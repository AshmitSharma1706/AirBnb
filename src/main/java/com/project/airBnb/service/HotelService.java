package com.project.airBnb.service;

import com.project.airBnb.dto.HotelDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface HotelService {

    HotelDto createHotel(HotelDto hotelDto);

    Optional<HotelDto> getHotelById(Long id);

    List<HotelDto> getAllHotels();

    HotelDto updateHotelById(Long id, Map<String, Object> updates);

    Boolean deleteHotelById(Long id);

    String activateHotelById(Long id);
}
