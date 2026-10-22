package com.example.hotelapi.dto;

public class AddressDto {

    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;

    public AddressDto() {
    }

    public AddressDto(
            String houseNumber,
            String street,
            String city,
            String country,
            String postCode
    ) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postCode = postCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostCode() {
        return postCode;
    }
}
