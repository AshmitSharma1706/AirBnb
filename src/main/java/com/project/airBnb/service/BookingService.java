package com.project.airBnb.service;

import com.project.airBnb.dto.BookingDto;
import com.project.airBnb.dto.BookingRequest;
import com.project.airBnb.dto.GuestDto;

import java.util.List;

public interface BookingService {
    BookingDto initiateBooking(BookingRequest request);

    BookingDto addGuests(Long id, List<GuestDto> guests);
}
