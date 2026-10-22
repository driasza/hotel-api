package com.example.hotelapi.dto;

public class ContactsDto {

    private String phone;
    private String email;

    public ContactsDto() {
    }

    public ContactsDto(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
