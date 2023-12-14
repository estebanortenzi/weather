package com.api.weather.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Metric {

  @JsonProperty("Value")
  private Double value;
  @JsonProperty("Unit")
  private String unit;
}
