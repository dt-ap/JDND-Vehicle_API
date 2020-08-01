package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PriceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/prices")
public class PricingController {

  private final PriceService service;

  public PricingController(PriceService service) {
    this.service = service;
  }

  /**
   * Gets the price for a requested vehicle.
   *
   * @param vehicleId ID number of the vehicle for which the price is requested
   * @return price of the vehicle, or error that it was not found.
   */
  @GetMapping
  public Price get(@RequestParam Long vehicleId) {
    try {
      return service.getPrice(vehicleId);
    } catch (PriceException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Price Not Found", ex);
    }

  }
}
