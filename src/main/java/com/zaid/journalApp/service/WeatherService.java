package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.Geocoding;
import com.zaid.journalApp.entity.Weather;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private static final String GEOCODING_API_URL = "https://api.openweathermap.org/geo/1.0/direct";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public Weather getWeatherForCity(String city) {
        try {
            Geocoding[] geocodingResponse = getGeocodingForCity(city);

            if(geocodingResponse == null || geocodingResponse.length == 0) {
                log.error("Failed to get geocoding for city: {}", city);
                return null;
            }

            Geocoding location = geocodingResponse[0];

            String weatherUri = UriComponentsBuilder.fromHttpUrl(WEATHER_URL)
                    .queryParam("lat", location.getLat())
                    .queryParam("lon", location.getLon())
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            log.info("Fetching weather for city: {}", city);
            ResponseEntity<Weather> response = restTemplate.exchange(
                    weatherUri,
                    HttpMethod.GET,
                    entity,
                    Weather.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Failed to get weather data. Status code: {}", response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            log.error("Error fetching weather for city: {}", city, e);
            return null;
        }
    }

    private Geocoding[] getGeocodingForCity(String city) {
        String geocodingUri = UriComponentsBuilder.fromHttpUrl(GEOCODING_API_URL)
                .queryParam("q", city)
                .queryParam("limit", 1)
                .queryParam("appid", apiKey)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<?> entity = new HttpEntity<>(headers);

        log.info("Fetching coordinates for city: {}", city);
        ResponseEntity<Geocoding[]> response = restTemplate.exchange(
                geocodingUri,
                HttpMethod.GET,
                entity,
                Geocoding[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Failed to get geocoding data. Status code: {}", response.getStatusCode());
            return null;
        }
    }
}