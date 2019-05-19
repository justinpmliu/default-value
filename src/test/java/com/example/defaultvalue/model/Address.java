package com.example.defaultvalue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String city;
    private String street;
    private Integer room;
    private Boolean primary;

}
