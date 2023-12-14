package com.api.weather.service;

import com.api.weather.model.city.CityDTO;

public interface CityService {

  CityDTO getCityInfo(String name);

}
