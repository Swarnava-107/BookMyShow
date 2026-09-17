package com.example.bookMyShow.service;

import com.example.bookMyShow.dto.*;
import com.example.bookMyShow.exception.ResourceNotFoundException;
import com.example.bookMyShow.exception.SeatUnavailableException;
import com.example.bookMyShow.model.*;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.repository.ShowSeatRepository;
import com.example.bookMyShow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingRequestDto) {

        User user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Show show = showRepository.findById(bookingRequestDto.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found"));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequestDto.getSeatIds());

        for(ShowSeat showSeat : selectedSeats)
        {
            if(!"AVAILABLE".equals(showSeat.getStatus())) {
                throw new SeatUnavailableException("Seat" + showSeat.getSeat().getSeatNo() + " is not available");
            }
            showSeat.setStatus("LOCKED");
        }
        showSeatRepository.saveAll(selectedSeats);

        double totalAmount = selectedSeats.stream().mapToDouble(ShowSeat::getPrice).sum();

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
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setStatus("CONFIRMED");
        booking.setPayment(payment);
        booking.setShowSeats(selectedSeats);
        booking.setTotalAmount(totalAmount);

        Booking savedBooking = bookingRepository.save(booking);

        selectedSeats.forEach(seat -> {
            seat.setStatus("BOOKED");
            seat.setBooking(savedBooking);
        });

        showSeatRepository.saveAll(selectedSeats);

        return mapToBookingDto(savedBooking, selectedSeats);

    }


    @Transactional
    public BookingDto getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found"));

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=(null) && seat.getBooking().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
        
    }


    @Transactional
    public BookingDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found"));

        booking.setStatus("CANCELLED");

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking() != null && !seat.getBooking().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat -> {
            seat.setStatus("CANCELLED");
            seat.setBooking(null);
        });

        if(booking.getPayment()!=null) {
            booking.getPayment().setPaymentStatus("REFUNDED");
        }

        Booking updatedBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);

        return mapToBookingDto(updatedBooking, seats);

    }


    // mapping to dto
    private BookingDto mapToBookingDto(Booking booking, List<ShowSeat> seats) {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        // user
        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());

        // set show
        ShowDto showDto = new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());

        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setDuration(booking.getShow().getMovie().getDuration());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        showDto.setMovie(movieDto);

        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getScreenName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        TheatreDto theatreDto = new TheatreDto();
        theatreDto.setId(booking.getShow().getScreen().getTheatre().getId());
        theatreDto.setName(booking.getShow().getScreen().getTheatre().getName());
        theatreDto.setAddress(booking.getShow().getScreen().getTheatre().getAddress());
        theatreDto.setCity(booking.getShow().getScreen().getTheatre().getCity());
        theatreDto.setTotalScreens(booking.getShow().getScreen().getTheatre().getTotalScreens());

        screenDto.setTheatre(theatreDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);


        List<ShowSeatDto> showSeatDto = seats.stream()
                .map(seat -> {
                        ShowSeatDto seatDto = new ShowSeatDto();
                        seatDto.setId(seat.getId());
                        seatDto.setStatus(seat.getStatus());
                        seatDto.setPrice(seat.getPrice());

                        SeatDto baseSeatDto = new SeatDto();
                        baseSeatDto.setId(seat.getSeat().getId());
                        baseSeatDto.setSeatNumber(seat.getSeat().getSeatNo());
                        baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                        baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                        seatDto.setSeat(baseSeatDto);
                        return seatDto;
                        })
                .collect(Collectors.toList());


        bookingDto.setSeats(showSeatDto);

        if(booking.getPayment() != null){
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDto.setPaymentDate(booking.getPayment().getPaymentDate());
            paymentDto.setStatus(booking.getPayment().getPaymentStatus());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
            bookingDto.setPayment(paymentDto);
        }

        return bookingDto;

    }
}
