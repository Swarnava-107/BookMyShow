package com.example.bookMyShow.repository;

import com.example.bookMyShow.model.Booking;
import com.example.bookMyShow.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<Booking> findByShowId(Long movieId);

   List<ShowSeat> findByShowIdAndStatus(Long showId, String status);

}
