package com.api.weather;

import com.api.weather.model.city.CityDTO;
import com.api.weather.model.weather.WeatherDTO;
import com.api.weather.repository.WeatherRepository;
import com.api.weather.service.CityService;
import com.api.weather.service.exception.AccuWeatherApiException;
import com.api.weather.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.api.weather.TestUtils.getCityDTO;
import static com.api.weather.TestUtils.getWeather;
import static com.api.weather.TestUtils.getWeatherDTO;
import static com.api.weather.TestUtils.getWeatherWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class WeatherServiceImplTest {

  @InjectMocks
  private WeatherServiceImpl weatherServiceImpl;

  @Mock
  private CityService cityService;

  @Mock
  private WeatherRepository weatherRepository;

  @Mock
  private RestTemplate restTemplate;

  private static final String API_KEY = "k6Wkv9lNlGqSnBV9nNegg48dSDlNv8K8";
  private static final String CITY_NAME_FAIL = "Test";
  private static final String CITY_NAME = "Mar del Plata";
  private static final String CITY_KEY = "7893";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(weatherServiceImpl, "apikey", "k6Wkv9lNlGqSnBV9nNegg48dSDlNv8K8");
  }

  @Test
  void getCurrentConditionsOfCity_SuccessfulResponse() {
    CityDTO mockCity = getCityDTO();
    Mockito.when(cityService.getCityInfo(CITY_NAME)).thenReturn(mockCity);

    WeatherDTO expectedWeather = getWeatherDTO();
    ResponseEntity<List<WeatherDTO>> mockResponse = new ResponseEntity<>(Collections.singletonList(expectedWeather), HttpStatus.OK);

    Mockito.when(restTemplate.exchange(
      eq("http://dataservice.accuweather.com/currentconditions/v1/" + CITY_KEY + "?apikey=" + API_KEY),
      eq(org.springframework.http.HttpMethod.GET),
      any(),
      eq(new ParameterizedTypeReference<List<WeatherDTO>>() {
      })
    )).thenReturn(mockResponse);

    Mockito.when(weatherRepository.save(getWeather())).thenReturn(getWeatherWithId());

    WeatherDTO resultWeather = weatherServiceImpl.getCurrentConditionsOfCity(CITY_NAME);

    assertNotNull(resultWeather);
    assertEquals(expectedWeather, resultWeather);

    Mockito.verify(weatherRepository, Mockito.times(1)).save(any());
  }

  @Test
  void getCurrentConditionsOfCity_EmptyResponse() {
    Mockito.when(cityService.getCityInfo(CITY_NAME_FAIL)).thenThrow(new AccuWeatherApiException("City not found"));

    assertThrows(AccuWeatherApiException.class, () -> weatherServiceImpl.getCurrentConditionsOfCity(CITY_NAME_FAIL));

    Mockito.verify(weatherRepository, Mockito.never()).save(any());
  }
}
