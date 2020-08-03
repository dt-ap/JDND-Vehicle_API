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
  public Price read(Long vehicleId) throws PriceException {
    return repository.findByVehicleId(vehicleId).orElseThrow(() -> new PriceException("Vehicle with id: '" + vehicleId + "' not found"));
  }

  public Price createOrUpdate(Price price) {
    return repository.save(price);
  }

  public void delete(Long vehicleId) throws PriceException {
    try {
      repository.deleteById(vehicleId);
    } catch (Exception ex) {
      throw new PriceException("Vehicle with id: '" + vehicleId + "' does not exists");
    }
  }
}
