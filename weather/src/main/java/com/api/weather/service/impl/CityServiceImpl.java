package com.api.weather.service.impl;

import com.api.weather.model.city.CityDTO;
import com.api.weather.service.CityService;
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
public class CityServiceImpl implements CityService {

  private static final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

  @Value("${accuweather.apikey}")
  private String apikey;

  private static final String ACCUWEATHER_API_URL = "http://dataservice.accuweather.com/locations/v1/cities/search";

  public CityDTO getCityInfo(String name) {
    try {
      String apiUrl = ACCUWEATHER_API_URL + "?apikey=" + apikey + "&q=" + name;
      RestTemplate restTemplate = new RestTemplate();

      ResponseEntity<List<CityDTO>> response = restTemplate.exchange(
        apiUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<CityDTO>>() {});

      if (!response.getStatusCode().is2xxSuccessful()) {
        handleErrorResponse(response);
      }

      List<CityDTO> cityList = response.getBody();

      if (cityList == null || cityList.isEmpty()) {
        log.error("City list from AccuWeather is empty or null.");
        throw new AccuWeatherApiException("City list from AccuWeather is empty or null.");
      }

      Optional<CityDTO> optional = cityList.stream().findFirst();

      return optional.orElse(null);

    } catch (Exception e) {
      log.error("Error getting list of cities from AccuWeather. {}", e.getMessage());
      throw new AccuWeatherApiException(e.getMessage(), e);
    }
  }

  private void handleErrorResponse(ResponseEntity<?> response) {
    log.error("Error getting list of cities from AccuWeather. Status code: {}", response.getStatusCode());
    throw new AccuWeatherApiException("Error getting list of cities from AccuWeather");
  }
}

