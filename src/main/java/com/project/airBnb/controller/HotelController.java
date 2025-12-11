package com.project.airBnb.controller;

import com.project.airBnb.advice.ApiResponse;
import com.project.airBnb.dto.BookingDto;
import com.project.airBnb.dto.HotelDto;
import com.project.airBnb.dto.HotelReportDto;
import com.project.airBnb.service.BookingService;
import com.project.airBnb.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotel")
public class HotelController {

    private final HotelService hotelService;

    private final BookingService bookingService;

    @PostMapping("/create")
    @Operation(summary = "Create a new hotel", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto){
        return new ResponseEntity<>(hotelService.createHotel(hotelDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a hotel by Id", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id){
        return new ResponseEntity<>(hotelService.getHotelById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all hotels owned by admin", tags = {"Admin Hotel"})
    private ResponseEntity<List<HotelDto>> getAllHotel(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        HotelDto hotel=hotelService.updateHotelById(id, updates);
        if(hotel==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hotel);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<ApiResponse<String>> activateHotelById(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(hotelService.activateHotelById(id)));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<ApiResponse<Boolean>> deleteHotelById(@PathVariable Long id){
        boolean deleted= hotelService.deleteHotelById(id);
        return deleted ? ResponseEntity.ok(new ApiResponse<>(true)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{hotelId}/bookings")
    @Operation(summary = "Get all bookings of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    @Operation(summary = "Generate a bookings report of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }
}
