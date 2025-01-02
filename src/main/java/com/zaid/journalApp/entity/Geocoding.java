package com.zaid.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geocoding {
    private double lat;
    private double lon;
    private String name;
    private String country;
    private String state;
}
