package com.udacity.pricing.domain.price;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PriceDTO {
  @Min(value = 1)
  protected Long vehicleId;

  @NotNull
  protected Currency currency;

  @NotNull
  protected BigDecimal value;

  public PriceDTO() {
  }

  public PriceDTO(Long vehicleId, Currency currency, BigDecimal value) {
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
