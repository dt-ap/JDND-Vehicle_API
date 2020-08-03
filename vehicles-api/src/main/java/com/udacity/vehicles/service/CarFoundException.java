package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Car already exists.")
public class CarFoundException extends RuntimeException {

  public CarFoundException() {
  }

  public CarFoundException(String message) {
    super(message);
  }
}
