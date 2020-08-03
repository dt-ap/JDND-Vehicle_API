package com.udacity.vehicles.domain.car;

import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Declares the Car class, related variables and methods.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Car {

  @Id
  @GeneratedValue
  private Long id;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Condition condition;

  @Valid
  @Embedded
  private Details details = new Details();

  @Valid
  @Embedded
  private Location location = new Location(0d, 0d);

  @Transient
  private Price price = new Price();

  public Car() {
  }

  public Car(Long id, @NotNull Condition condition, @Valid Details details, @Valid Location location, Price price) {
    this.id = id;
    this.condition = condition;
    this.details = details;
    this.location = location;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  public void setModifiedAt(LocalDateTime modifiedAt) {
    this.modifiedAt = modifiedAt;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  public Details getDetails() {
    return details;
  }

  public void setDetails(Details details) {
    this.details = details;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(@Valid Price price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Car car = (Car) o;
    return id.equals(car.id) &&
        createdAt.equals(car.createdAt) &&
        Objects.equals(modifiedAt, car.modifiedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, modifiedAt);
  }
}
