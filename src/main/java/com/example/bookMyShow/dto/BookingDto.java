package com.example.bookMyShow.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;
    private String bookingNumber;
    private LocalDateTime bookingTime;
    private ShowDto show;
    private UserDto user;
    private String status;
    private double totalPrice;
    private List<ShowSeatDto> seats;
    private PaymentDto payment;

}
