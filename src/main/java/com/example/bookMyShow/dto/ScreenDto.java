package com.example.bookMyShow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDto {

    private String id;
    private String name;
    private Integer totalSeats;
    private TheatreDto theatre;
}
