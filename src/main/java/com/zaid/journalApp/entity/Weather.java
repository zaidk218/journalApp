package com.zaid.journalApp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private MainData main;
    private WindData wind;
    private int visibility;
    private String name;
    private SysData sys;
    private WeatherDescription[] weather;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainData {
        private double temp;
        private int humidity;
        private int pressure;

        @JsonProperty("feels_like")
        private double feelLike;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;

        @JsonProperty("grnd_level")
        private int groundLevel;

        @JsonProperty("sea_level")
        private int seaLevel;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WindData {
        private double speed;
        private int deg;
        private double gust;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SysData {
        private String country;
        private long sunrise;
        private long sunset;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDescription {
        private String main;
        private String description;
        private String icon;
    }
}