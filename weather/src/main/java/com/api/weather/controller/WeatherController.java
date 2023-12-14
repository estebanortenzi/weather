package com.api.weather.controller;

import com.api.weather.model.weather.WeatherDTO;
import com.api.weather.service.WeatherService;
import com.api.weather.service.exception.AccuWeatherApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
public class WeatherController {

  @Autowired
  private WeatherService weatherService;

  @GetMapping(value = "/weather/{cityName}")
  public ResponseEntity<WeatherDTO> getCurrentConditionsOfCity(@PathVariable(value = "cityName") String cityName) {

    if(cityName.isEmpty()) throw new AccuWeatherApiException("Error getting current condition, text is empty: " + cityName);

    return ResponseEntity.ok().body(weatherService.getCurrentConditionsOfCity(cityName));
  }
}
