package com.example.bookMyShow.repository;

import com.example.bookMyShow.model.Booking;
import com.example.bookMyShow.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {

   List<Theatre> findByCity(String city);

}
