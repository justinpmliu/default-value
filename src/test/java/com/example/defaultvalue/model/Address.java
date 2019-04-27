package com.example.defaultvalue.model;

import lombok.Data;

@Data
public class Address {
    private String city;
    private String street;
    private Integer room;
    private Boolean primary;

    public Address(String city, String street) {
        this.city = city;
        this.street = street;
    }
}
