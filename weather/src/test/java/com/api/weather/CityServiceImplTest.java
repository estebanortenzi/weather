package com.api.weather;

import com.api.weather.model.city.CityDTO;
import com.api.weather.service.exception.AccuWeatherApiException;
import com.api.weather.service.impl.CityServiceImpl;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class CityServiceImplTest {

  @InjectMocks
  private CityServiceImpl cityServiceImpl;

  @Mock
  private RestTemplate restTemplate;

  private static final String API_KEY = "k6Wkv9lNlGqSnBV9nNegg48dSDlNv8K8";
  private static final String CITY_NAME = "Mar del Plata";
  private static final String CITY_NAME_FAIL = "Test";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(cityServiceImpl, "apikey", "k6Wkv9lNlGqSnBV9nNegg48dSDlNv8K8");
  }

  @Test
  void getCityInfo_SuccessfulResponse() {
    CityDTO expectedCity = getCityDTO();
    ResponseEntity<List<CityDTO>> mockResponse = new ResponseEntity<>(Collections.singletonList(expectedCity), HttpStatus.OK);

    Mockito.when(restTemplate.exchange(
      eq("http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + CITY_NAME),
      eq(org.springframework.http.HttpMethod.GET),
      any(),
      eq(new ParameterizedTypeReference<List<CityDTO>>() {
      })
    )).thenReturn(mockResponse);

    CityDTO resultCity = cityServiceImpl.getCityInfo(CITY_NAME);

    assertNotNull(resultCity);
    assertEquals(expectedCity, resultCity);
  }

  @Test
  void getCityInfo_EmptyResponse() {
    ResponseEntity<List<CityDTO>> mockResponse = new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);

    Mockito.when(restTemplate.exchange(
      eq("http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + CITY_NAME_FAIL),
      eq(org.springframework.http.HttpMethod.GET),
      any(),
      eq(new ParameterizedTypeReference<List<CityDTO>>() {
      })
    )).thenReturn(mockResponse);

    assertThrows(AccuWeatherApiException.class, () -> cityServiceImpl.getCityInfo(CITY_NAME_FAIL));
  }
}
