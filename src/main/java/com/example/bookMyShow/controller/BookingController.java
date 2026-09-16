package com.example.bookMyShow.controller;

import com.example.bookMyShow.dto.BookingDto;
import com.example.bookMyShow.dto.BookingRequestDto;
import com.example.bookMyShow.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto), HttpStatus.CREATED);
    }
}
