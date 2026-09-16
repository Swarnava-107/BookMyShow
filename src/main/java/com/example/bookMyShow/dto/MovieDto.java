package com.example.bookMyShow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long id;
    private String title;
    private String description;
    private String language;
    private String genre;
    private String duration;
    private String releaseDate;
    private String posterUrl;
}
