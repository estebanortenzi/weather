package com.api.weather.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Temperature {
  @JsonProperty("Metric")
  private Metric metric;
}
