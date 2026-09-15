package com.example.bookMyShow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "theatres")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;
    private String totalScreen;

    @OneToMany(mappedBy = "theatre",  cascade = CascadeType.ALL)
    private List<Screen> screens;
}
