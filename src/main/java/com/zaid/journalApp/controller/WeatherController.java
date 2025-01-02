package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.Weather;
import com.zaid.journalApp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<Weather> getWeather(@RequestParam String city) {
        log.info("Received request to fetch weather for city: {}", city);
        Weather weather = weatherService.getWeatherForCity(city);

        if(weather!=null) {
            log.info("Fetched weather for city: {}", city);
            return ResponseEntity.ok(weather);
        }else{
            log.warn("No data found for the city :{}", city);
            return ResponseEntity.notFound().build();
        }
    }


}
