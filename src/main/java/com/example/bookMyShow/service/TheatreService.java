package com.example.bookMyShow.service;

import com.example.bookMyShow.dto.TheatreDto;
import com.example.bookMyShow.exception.ResourceNotFoundException;
import com.example.bookMyShow.model.Theatre;
import com.example.bookMyShow.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatreService {

    private TheatreRepository theatreRepository;

    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public TheatreDto createTheatre(TheatreDto theatreDto) {
        Theatre theatre = mapToEntity(theatreDto);
        Theatre savedTheatre = theatreRepository.save(theatre);
        return mapToDto(savedTheatre);
    }

    public TheatreDto getTheatre(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("theatre not found with id: " + id));
        return mapToDto(theatre);
    }

    public List<TheatreDto> getAllTheatre() {
        List<Theatre> theatreList = theatreRepository.findAll();
        return theatreList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TheatreDto> getTheatreByCity(String city) {
        List<Theatre> theatreList = theatreRepository.findByCity(city);
        return theatreList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    private TheatreDto mapToDto(Theatre savedTheatre) {
        TheatreDto theatreDto = new TheatreDto();
        theatreDto.setId(savedTheatre.getId());
        theatreDto.setName(savedTheatre.getName());
        theatreDto.setAddress(savedTheatre.getAddress());
        theatreDto.setCity(savedTheatre.getCity());
        theatreDto.setTotalScreens(savedTheatre.getTotalScreens());
        return theatreDto;
    }

    private Theatre mapToEntity(TheatreDto theatreDto) {
        Theatre theatre = new Theatre();
        theatre.setId(theatreDto.getId());
        theatre.setName(theatreDto.getName());
        theatre.setAddress(theatreDto.getAddress());
        theatre.setCity(theatreDto.getCity());
        theatre.setTotalScreens(theatreDto.getTotalScreens());
        return theatre;
    }
}
