package com.udacity.pricing.domain.price;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
public class Price {

  @Id
  private Long vehicleId;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Currency currency;

  @NotNull
  private BigDecimal price;

  public Price() {
  }

  public Price(@NotNull Long vehicleId, @NotNull Currency currency, @NotNull BigDecimal price) {
    this.vehicleId = vehicleId;
    this.currency = currency;
    this.price = price;
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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

}
