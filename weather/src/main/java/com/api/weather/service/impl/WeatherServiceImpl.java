package com.api.weather.service.impl;

import com.api.weather.model.city.CityDTO;
import com.api.weather.model.weather.Weather;
import com.api.weather.model.weather.WeatherDTO;
import com.api.weather.repository.WeatherRepository;
import com.api.weather.service.CityService;
import com.api.weather.service.WeatherService;
import com.api.weather.service.exception.AccuWeatherApiException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService {

  private static final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

  private final CityService cityService;

  private final WeatherRepository weatherRepository;

  private static final String ACCUWEATHER_API_URL = "http://dataservice.accuweather.com/currentconditions/v1/";

  @Value("${accuweather.apikey}")
  private String apikey;

  public WeatherServiceImpl(CityService cityService,
    WeatherRepository weatherRepository) {
    this.cityService = cityService;
    this.weatherRepository = weatherRepository;
  }

  @Override
  public WeatherDTO getCurrentConditionsOfCity(String name) {
    CityDTO cityDTO = cityService.getCityInfo(name);

    try {
      String apiUrl = ACCUWEATHER_API_URL + cityDTO.getKey() + "?apikey=" + apikey;

      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<List<WeatherDTO>> response = restTemplate.exchange(
        apiUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {});

      if (!response.getStatusCode().is2xxSuccessful()) {
        handleErrorResponse(response);
      }

      List<WeatherDTO> weatherList = response.getBody();

      if (weatherList == null || weatherList.isEmpty()) {
        handleEmptyWeatherList();
      }

      Optional<WeatherDTO> optional = weatherList.stream().findFirst();

      if (optional.isPresent()) {
        Weather weather = mapWeatherDTOToEntity(optional.get());
        weatherRepository.save(weather);
      }

      return optional.orElse(null);

    } catch (Exception e) {
      log.error("Error getting Current condition from AccuWeather. {}", e.getMessage());
      throw new AccuWeatherApiException(e.getMessage(), e);
    }
  }

  private void handleErrorResponse(ResponseEntity<?> response) {
    log.error("Error getting Current condition from AccuWeather. Status code: {}", response.getStatusCode());
    throw new AccuWeatherApiException("Error getting Current condition from AccuWeather");
  }

  private void handleEmptyWeatherList() {
    log.error("Current condition from AccuWeather is empty or null.");
    throw new AccuWeatherApiException("Current condition from AccuWeather is empty or null.");
  }

  private Weather mapWeatherDTOToEntity(WeatherDTO weatherDTO) {
    Weather weather = new Weather();
    weather.setHasPrecipitation(weatherDTO.getHasPrecipitation());
    weather.setWeatherType(weatherDTO.getWeatherType());
    weather.setTemperature(weatherDTO.getTemperature().getMetric().getValue());
    return weather;
  }
}
