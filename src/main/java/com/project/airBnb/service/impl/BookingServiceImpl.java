package com.project.airBnb.service.impl;

import com.project.airBnb.dto.BookingDto;
import com.project.airBnb.dto.BookingRequest;
import com.project.airBnb.dto.GuestDto;
import com.project.airBnb.entity.*;
import com.project.airBnb.entity.enums.BookingStatus;
import com.project.airBnb.exception.ResourceNotFoundException;
import com.project.airBnb.repository.*;
import com.project.airBnb.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;

    private final BookingRepository bookingRepository;

    private final HotelRepository hotelRepository;

    private  final RoomRepository roomRepository;

    private final InventoryRepository inventoryRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initiateBooking(BookingRequest request) {
        Hotel hotel=hotelRepository.findById(request.getHotelId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id "+request.getHotelId()));

        Room room=roomRepository.findById(request.getRoomId()).orElseThrow(() ->
                new ResourceNotFoundException("Room not found with id "+request.getRoomId()));

        List<Inventory> inventories=inventoryRepository
                .findAndLockAvailableInventory(room.getId(), request.getCheckInDate()
                        , request.getCheckOutDate(), request.getRoomsCount());

        long days= ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate())+1;
        if(inventories.size() != days){
            throw  new IllegalStateException("Room is not available anyroom");
        }
        for(Inventory i: inventories){
            i.setReservedCount(i.getBookedCount() + request.getRoomsCount());
        }
        inventoryRepository.saveAll(inventories);

        //TODO: calculate dynamic amount

        Booking booking= Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(request.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long id, List<GuestDto> guests) {
        Booking booking=bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id "+id));
        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired");
        }
        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }
        for(GuestDto g:guests){
            Guest guest=modelMapper.map(g, Guest.class);
            guest.setUser(getCurrentUser());
            guest=guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking=bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    private User getCurrentUser() {
        User user=new User();//TODO: Remove dummy user
        user.setId(1L);
        return user;
    }

    private boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }
}
