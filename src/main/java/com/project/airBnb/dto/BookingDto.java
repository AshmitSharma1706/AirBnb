package com.project.airBnb.dto;

import com.project.airBnb.entity.Guest;
import com.project.airBnb.entity.Hotel;
import com.project.airBnb.entity.Room;
import com.project.airBnb.entity.User;
import com.project.airBnb.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;

    private Hotel hotel;

    private Room room;

    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;
}
