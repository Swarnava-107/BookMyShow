package com.example.bookMyShow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNo;

    @Column(nullable = false)
    private String seatType;

    @Column(nullable = false)
    private Double basePrice;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
}
