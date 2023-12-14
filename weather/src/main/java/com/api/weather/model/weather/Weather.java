package com.api.weather.model.weather;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "weather")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "weather_type")
  private String weatherType;

  @Column(name = "has_precipitation")
  private Boolean hasPrecipitation;

  @Column(name = "temperature")
  private Double temperature;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Weather weather = (Weather) o;
    return id != null && Objects.equals(id, weather.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
