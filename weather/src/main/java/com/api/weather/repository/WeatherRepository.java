package com.api.weather.repository;

import com.api.weather.model.weather.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {

}
