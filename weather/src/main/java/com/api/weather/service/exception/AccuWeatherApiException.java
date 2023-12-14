package com.api.weather.service.exception;

public class AccuWeatherApiException extends RuntimeException {

  public AccuWeatherApiException(String message) {
    super(message);
  }

  public AccuWeatherApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
