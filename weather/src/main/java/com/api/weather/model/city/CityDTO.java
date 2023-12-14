package com.api.weather.model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityDTO {

  @JsonProperty("Key")
  private String key;
  @JsonProperty("EnglishName")
  private String englishName;

}
