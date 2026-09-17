package com.example.bookMyShow.service;

import com.example.bookMyShow.dto.MovieDto;
import com.example.bookMyShow.exception.ResourceNotFoundException;
import com.example.bookMyShow.model.Movie;
import com.example.bookMyShow.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private MovieRepository movieRepository;
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = mapToEntity(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return maptToDto(savedMovie);
    }


    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return maptToDto(movie);
    }


    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::maptToDto)
                .collect(Collectors.toList());
    }


    public List<MovieDto> getMoviesByLanguage(String language) {
        List<Movie> movies = movieRepository.findByLanguage(language);
        return movies.stream().map(this::maptToDto).collect(Collectors.toList());
    }

    public List<MovieDto> getMoviesByGenre(String genre) {
        List<Movie> movies = movieRepository.findByLanguage(genre);
        return movies.stream().map(this::maptToDto).collect(Collectors.toList());
    }

    public List<MovieDto> searchMovie(String title) {
        List<Movie> movies = movieRepository.findByLanguage(title);
        return movies.stream().map(this::maptToDto).collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDescription(movieDto.getDescription());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setReleaseDate(movieDto.getReleaseDate());

        Movie updatedMovie = movieRepository.save(movie);
        return maptToDto(updatedMovie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        movieRepository.delete(movie);
    }



    public MovieDto maptToDto(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDescription(movie.getDescription());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDuration(movie.getDuration());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }

    public Movie mapToEntity(MovieDto movieDto) {

        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDescription(movieDto.getDescription());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setReleaseDate(movieDto.getReleaseDate());
        return movie;

    }

}
