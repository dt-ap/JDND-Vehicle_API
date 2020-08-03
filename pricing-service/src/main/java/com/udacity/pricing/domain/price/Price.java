package com.udacity.pricing.domain.price;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vehicleId", "currency"})
})
public class Price {

  @Id
  private Long vehicleId;

  @NotNull
  @Column(length = 3)
  @Enumerated(EnumType.STRING)
  private Currency currency;

  @NotNull
  private BigDecimal value;

  public Price() {
  }

  public Price(Long vehicleId, @NotNull Currency currency, @NotNull BigDecimal value) {
    this.vehicleId = vehicleId;
    this.currency = currency;
    this.value = value;
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
