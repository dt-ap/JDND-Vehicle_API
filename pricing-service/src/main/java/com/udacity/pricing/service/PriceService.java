package com.udacity.pricing.service;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

  private final PriceRepository repository;

  public PriceService(PriceRepository repository) {
    this.repository = repository;
  }

  /**
   * If a valid vehicle ID, gets the price of the vehicle from the stored array.
   *
   * @param vehicleId ID number of the vehicle the price is requested for.
   * @return price of the requested vehicle
   * @throws PriceException vehicleID was not found
   */
  public Price getPrice(Long vehicleId) throws PriceException {
    return repository.findOneByVehicleId(vehicleId).orElseThrow(() -> new PriceException("Vehicle with id: " + vehicleId + "not found"));
  }
}
