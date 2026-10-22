package com.example.hotelapi.mapper;

import com.example.hotelapi.dto.*;
import com.example.hotelapi.entity.Address;
import com.example.hotelapi.entity.Hotel;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public HotelSummaryDto toSummaryDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        return new HotelSummaryDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                formatAddress(hotel),
                hotel.getPhone()
        );
    }

    public HotelDetailDto toDetailDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        Address address = hotel.getAddress();

        AddressDto addressDto = address == null ? null : new AddressDto(
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getCountry(),
                address.getPostCode()
        );

        ContactsDto contactsDto = new ContactsDto(
                hotel.getPhone(),
                hotel.getEmail()
        );

        ArrivalTimeDto arrivalTimeDto = new ArrivalTimeDto(
                hotel.getCheckIn() == null ? null : hotel.getCheckIn().toString(),
                hotel.getCheckOut() == null ? null : hotel.getCheckOut().toString()
        );

        List<String> amenities = hotel.getAmenities() == null
                ? List.of()
                : hotel.getAmenities().stream()
                .map(amenity -> amenity.getName())
                .collect(Collectors.toList());

        return new HotelDetailDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getBrand(),
                addressDto,
                contactsDto,
                arrivalTimeDto,
                amenities
        );
    }

    private String formatAddress(Hotel hotel) {
        Address address = hotel.getAddress();

        if (address == null) {
            return "";
        }

        return java.util.stream.Stream.of(
                        address.getHouseNumber() == null
                                ? null
                                : address.getHouseNumber().toString(),
                        address.getStreet(),
                        address.getCity(),
                        address.getCountry(),
                        address.getPostCode()
                )
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.joining(", "));
    }

    public Hotel toEntity(CreateHotelRequest request) {
        Hotel hotel = new Hotel();

        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setBrand(request.getBrand());

        if (request.getAddress() != null) {
            Address address = new Address();
            address.setHouseNumber(request.getAddress().getHouseNumber());
            address.setStreet(request.getAddress().getStreet());
            address.setCity(request.getAddress().getCity());
            address.setCountry(request.getAddress().getCountry());
            address.setPostCode(request.getAddress().getPostCode());
            hotel.setAddress(address);
        }

        if (request.getContacts() != null) {
            hotel.setPhone(request.getContacts().getPhone());
            hotel.setEmail(request.getContacts().getEmail());
        }

        if (request.getArrivalTime() != null) {
            String checkIn = request.getArrivalTime().getCheckIn();
            String checkOut = request.getArrivalTime().getCheckOut();

            if (checkIn != null && !checkIn.isBlank()) {
                hotel.setCheckIn(LocalTime.parse(checkIn));
            }

            if (checkOut != null && !checkOut.isBlank()) {
                hotel.setCheckOut(LocalTime.parse(checkOut));
            }
        }

        return hotel;
    }
}