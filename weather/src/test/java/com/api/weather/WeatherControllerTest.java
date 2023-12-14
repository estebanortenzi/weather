package com.api.weather;

import com.api.weather.controller.WeatherController;
import com.api.weather.model.weather.WeatherDTO;
import com.api.weather.service.WeatherService;
import com.api.weather.service.exception.AccuWeatherApiException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.api.weather.TestUtils.getWeatherDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class WeatherControllerTest {

  @Mock
  private WeatherService weatherService;

  @InjectMocks
  private WeatherController weatherController;

  public WeatherControllerTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetCurrentConditionsOfCity() {
    String cityName = "TestCity";
    WeatherDTO weatherDTO = getWeatherDTO();

    when(weatherService.getCurrentConditionsOfCity(anyString())).thenReturn(weatherDTO);

    ResponseEntity<WeatherDTO> responseEntity = weatherController.getCurrentConditionsOfCity(cityName);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(weatherDTO, responseEntity.getBody());
  }

  @Test
  void testGetCurrentConditionsOfCity_WithEmptyCityName() {
    String cityName = "";
    when(weatherService.getCurrentConditionsOfCity(anyString())).thenThrow(new AccuWeatherApiException("Error getting current condition, text is empty: " + cityName));

    assertThrows(AccuWeatherApiException.class, () -> weatherController.getCurrentConditionsOfCity(cityName));
  }
}
