package com.example.hotelapi.dto;

import java.util.List;

public class HotelDetailDto {

    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressDto address;
    private ContactsDto contacts;
    private ArrivalTimeDto arrivalTime;
    private List<String> amenities;

    public HotelDetailDto() {
    }

    public HotelDetailDto(
            Long id,
            String name,
            String description,
            String brand,
            AddressDto address,
            ContactsDto contacts,
            ArrivalTimeDto arrivalTime,
            List<String> amenities
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.address = address;
        this.contacts = contacts;
        this.arrivalTime = arrivalTime;
        this.amenities = amenities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public AddressDto getAddress() {
        return address;
    }

    public ContactsDto getContacts() {
        return contacts;
    }

    public ArrivalTimeDto getArrivalTime() {
        return arrivalTime;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}
