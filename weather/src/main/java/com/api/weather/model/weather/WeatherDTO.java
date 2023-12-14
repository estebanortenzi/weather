package com.api.weather.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherDTO {

  private Integer id;

  @JsonProperty("WeatherText")
  private String weatherType;
  @JsonProperty("HasPrecipitation")
  private Boolean hasPrecipitation;
  @JsonProperty("Temperature")
  private Temperature temperature;

}
