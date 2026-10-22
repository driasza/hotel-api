package com.example.hotelapi.repository;

import com.example.hotelapi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByHotelId(Long hotelId);
}
