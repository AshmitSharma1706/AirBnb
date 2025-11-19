package com.project.airBnb.controller;

import com.project.airBnb.dto.BookingDto;
import com.project.airBnb.dto.BookingRequest;
import com.project.airBnb.dto.GuestDto;
import com.project.airBnb.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/initiate")
    public ResponseEntity<BookingDto> initiateBooking(@RequestBody BookingRequest request){
        return ResponseEntity.ok(bookingService.initiateBooking(request));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(
            @PathVariable(name = "bookingId") Long id, @RequestBody List<GuestDto> guests){
        return ResponseEntity.ok(bookingService.addGuests(id,guests));
    }
}
