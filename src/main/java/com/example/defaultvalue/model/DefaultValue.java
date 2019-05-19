package com.example.defaultvalue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultValue {
    private int id;
    private String service;
    private String clazz;
    private String field;
    private String value;
}

