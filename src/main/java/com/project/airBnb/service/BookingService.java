package com.project.airBnb.service;

import com.project.airBnb.dto.BookingDto;
import com.project.airBnb.dto.BookingRequest;
import com.project.airBnb.dto.GuestDto;
import com.stripe.model.Event;

import java.util.List;

public interface BookingService {
    BookingDto initiateBooking(BookingRequest request);

    BookingDto addGuests(Long id, List<GuestDto> guests);

    String initiatePayment(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
