package com.example.hotelapi.dto;

public class ArrivalTimeDto {

    private String checkIn;
    private String checkOut;

    public ArrivalTimeDto() {
    }

    public ArrivalTimeDto(String checkIn, String checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }
}
