package com.example.hotelapi.repository;

import com.example.hotelapi.entity.Hotel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends
        JpaRepository<Hotel, Long>{

    @Override
    @EntityGraph(attributePaths = "amenities")
    List<Hotel> findAll();

    @Override
    @EntityGraph(attributePaths = "amenities")
    Optional<Hotel> findById(Long id);
}
