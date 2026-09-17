package com.example.bookMyShow.service;

import com.example.bookMyShow.dto.*;
import com.example.bookMyShow.exception.ResourceNotFoundException;
import com.example.bookMyShow.model.Movie;
import com.example.bookMyShow.model.Screen;
import com.example.bookMyShow.model.Show;
import com.example.bookMyShow.model.ShowSeat;
import com.example.bookMyShow.repository.MovieRepository;
import com.example.bookMyShow.repository.ScreenRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.repository.ShowSeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private MovieRepository movieRepository;
    private ScreenRepository screenRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;


    public ShowService(MovieRepository movieRepository, ScreenRepository screenRepository,
                       ShowRepository showRepository, ShowSeatRepository showSeatRepository) {
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
    }

    public ShowDto createShow(ShowDto showDto) {
        Show show = new Show();
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");

        return mapToDto(savedShow,availableSeats);

    }


    public ShowDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show,availableSeats);
    }

    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByMovieAndCity(Long movieId,String city) {
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theatre_City(movieId,city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Show> shows = showRepository.findByStartTimeBetween(startDate, endDate);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }



    private ShowDto mapToDto(Show show, List<ShowSeat> availableSeats) {
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl(),
                show.getMovie().getDuration()
        ));

        TheatreDto theatreDto = new TheatreDto(
                show.getScreen().getTheatre().getId(),
                show.getScreen().getTheatre().getName(),
                show.getScreen().getTheatre().getAddress(),
                show.getScreen().getTheatre().getCity(),
                show.getScreen().getTheatre().getTotalScreens()
        );

        showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getScreenName(),
                show.getScreen().getTotalSeats(),
                theatreDto
        ));

        List<ShowSeatDto> seatDtos = availableSeats.stream()
                .map(seat -> {
                    ShowSeatDto seatDto = new ShowSeatDto();
                    seatDto.setId(seat.getId());
                    seatDto.setStatus(seat.getStatus());
                    seatDto.setPrice(seat.getPrice());

                    SeatDto basicSeatDto = new SeatDto();
                    basicSeatDto.setId(seat.getSeat().getId());
                    basicSeatDto.setSeatNumber(seat.getSeat().getSeatNo());
                    basicSeatDto.setSeatType(seat.getSeat().getSeatType());
                    basicSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    seatDto.setSeat(basicSeatDto);
                    return seatDto;
                })
                .collect(Collectors.toList());

        showDto.setAvailableSeats(seatDtos);
        return showDto;
    }
}
