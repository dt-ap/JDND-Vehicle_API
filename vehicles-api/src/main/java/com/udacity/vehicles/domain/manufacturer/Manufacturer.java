package com.udacity.vehicles.domain.manufacturer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Declares class to hold car manufacturer information.
 */
@Entity
public class Manufacturer {

  @Id
  private Integer code;

  @NotNull
  @Column(unique = true)
  private String name;

  public Manufacturer() { }

  public Manufacturer(Integer code, String name) {
    this.code = code;
    this.name = name;
  }

  public Integer getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
