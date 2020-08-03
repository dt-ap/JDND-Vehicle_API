package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceDTO;
import com.udacity.pricing.domain.price.PriceMapper;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PriceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/prices")
public class PricingController {

  private final PriceService service;
  private final PriceMapper mapper;

  public PricingController(PriceService service, PriceMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PostMapping
  public Price post(@RequestBody @Valid PriceDTO dto) {
    try {
      return service.createOrUpdate(mapper.toEntity(dto));
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }
  }

  /**
   * Gets the price for a requested vehicle.
   *
   * @param vehicleId ID number of the vehicle for which the price is requested
   * @return price of the vehicle, or error that it was not found.
   */
  @GetMapping("/{vehicleId}")
  public Price get(@PathVariable Long vehicleId) {
    try {
      return service.read(vehicleId);
    } catch (PriceException ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }
  }

  @DeleteMapping("/{vehicleId}")
  public void delete(@PathVariable Long vehicleId) {
    try {
      service.delete(vehicleId);
    } catch (PriceException ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }
  }
}
