package com.api.weather;

import com.api.weather.model.city.CityDTO;
import com.api.weather.model.weather.Metric;
import com.api.weather.model.weather.Temperature;
import com.api.weather.model.weather.Weather;
import com.api.weather.model.weather.WeatherDTO;

public class TestUtils {

  public static CityDTO getCityDTO(){
    return CityDTO.builder()
      .key("7893")
      .englishName("Mar del Plata")
      .build();
  }

  public static Weather getWeather(){
    return Weather.builder()
      .hasPrecipitation(true)
      .weatherType("Sunny")
      .temperature(getMetric().getValue())
      .build();
  }

  public static Weather getWeatherWithId(){
    return Weather.builder()
      .id(1)
      .hasPrecipitation(true)
      .weatherType("Sunny")
      .temperature(getMetric().getValue())
      .build();
  }

  public static WeatherDTO getWeatherDTO(){
    WeatherDTO weatherDTO = new WeatherDTO();
    weatherDTO.setWeatherType("Mostly sunny");
    weatherDTO.setHasPrecipitation(false);
    weatherDTO.setTemperature(getTemperature());

    return weatherDTO;
  }

  public static Temperature getTemperature(){
    Temperature temperature = new Temperature();
    temperature.setMetric(getMetric());
    return temperature;
  }

  public static Metric getMetric(){
    Metric metric = new Metric();
    metric.setUnit("C");
    metric.setValue(20.2D);
    return metric;
  }

}
