package com.api.weather.service;

import com.api.weather.model.weather.WeatherDTO;

public interface WeatherService {

  WeatherDTO getCurrentConditionsOfCity(String cityName);
}
