package com.udacity.vehicles.client.prices;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
public class Price {
  @Min(value = 1)
  private Long vehicleId;

  @NotNull
  private Currency currency;

  @NotNull
  private BigDecimal value;

  public Price() {
  }

  public Price(Long vehicleId, Currency currency, BigDecimal value) {
    this.vehicleId = vehicleId;
    this.currency = currency;
    this.value = value;
  }

  @JsonIgnore
  public boolean isPresent() {
    return currency != null && value != null;
  }

  public Long getVehicleId() {
    return vehicleId;
  }

  public void setVehicleId(Long vehicleId) {
    this.vehicleId = vehicleId;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }
}
