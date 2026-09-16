package com.example.bookMyShow.service;

import com.example.bookMyShow.dto.BookingDto;
import com.example.bookMyShow.dto.BookingRequestDto;
import com.example.bookMyShow.exception.ResourceNotFoundException;
import com.example.bookMyShow.exception.SeatUnavailableException;
import com.example.bookMyShow.model.*;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.repository.ShowSeatRepository;
import com.example.bookMyShow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private BookingRepository bookingRepository;

    public BookingService(UserRepository userRepository, ShowRepository showRepository,
                          ShowSeatRepository showSeatRepository,  BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
    }

    public BookingDto createBooking(BookingRequestDto bookingRequestDto) {

        User user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Show show = showRepository.findById(bookingRequestDto.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found"));

        List<ShowSeat> showSeatList = showSeatRepository.findAllById(bookingRequestDto.getSeatIds());

        for(ShowSeat showSeat : showSeatList)
        {
            if(!"AVAILABLE".equals(showSeat.getStatus())) {
                throw new SeatUnavailableException("Seat" + showSeat.getSeat().getSeatNo() + " is not available");
            }
            showSeat.setStatus("LOCKED");
        }
        showSeatRepository.saveAll(showSeatList);

        double totalAmount = showSeatList.stream().mapToDouble(ShowSeat::getPrice).sum();

        // payment generated
        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(bookingRequestDto.getPaymentMethod());
        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        // booking generation
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingNo(UUID.randomUUID().toString());
        booking.setStatus("CONFIRMED");
        booking.setPayment(payment);
        booking.setShowSeats(showSeatList);
        booking.setTotalAmount(totalAmount);

        Booking savedBooking = bookingRepository.save(booking);

        showSeatList.forEach(seat -> {
            seat.setStatus("BOOKED");
            seat.setBooking(savedBooking);
        });

        showSeatRepository.saveAll(showSeatList);

        return mapToBookingDto(booking, showSeatList);

    }

    private BookingDto mapToBookingDto(Booking booking, List<ShowSeat> seats) {

    }
}
