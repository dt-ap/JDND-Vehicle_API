package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

  private final CarRepository repository;
  private final MapsClient mapsClient;
  private final PriceClient priceClient;

  public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
    this.repository = repository;
    this.mapsClient = mapsClient;
    this.priceClient = priceClient;
  }

  /**
   * Gathers a list of all vehicles
   *
   * @return a list of all vehicles in the CarRepository
   */
  public List<Car> readAll() {
    return repository.findAll();
  }

  /**
   * Gets car information by ID (or throws exception if non-existent)
   *
   * @param id the ID number of the car to gather information on
   * @return the requested car's information, including location and price
   */
  public Car read(Long id) throws CarNotFoundException {
    return repository.findById(id).map(
        car -> {
          var price = priceClient.get(id);
          var location = mapsClient.get(car.getLocation());
          car.setPrice(price);
          car.setLocation(location);
          return car;
        }
    ).orElseThrow(CarNotFoundException::new);
  }

  /**
   * Creates a vehicle
   *
   * @param car A car object, which can be either new or existing
   * @return the new car is stored in the repository
   */
  public Car create(Car car) throws CarFoundException {
    var id = car.getId();
    var price = car.getPrice();
    var location = car.getLocation();

    if (id != null && repository.existsById(id)) {
      throw new CarFoundException("Vehicle with id '" + id + "' already exists");
    }
    var saved = repository.save(car);
    if (price.isPresent()) {
      price.setVehicleId(saved.getId());
      saved.setPrice(priceClient.post(price));
    }
    if (location.isPresent()) {
      saved.setLocation(mapsClient.get(location));
    }

    return saved;
  }

  public Car update(Car updatedCar) throws CarNotFoundException {
    var updatedId = updatedCar.getId();
    if (updatedId != null) {
      return repository.findById(updatedId).map(
          car -> {
            var updatedPrice = updatedCar.getPrice();
            var updatedLocation = updatedCar.getLocation();

            if (updatedPrice.isPresent()) {
              updatedPrice.setVehicleId(updatedId);
              car.setPrice(priceClient.post(updatedPrice));
            }
            if (updatedLocation.isPresent()) {
              car.setLocation(mapsClient.get(updatedLocation));
            }

            car.setDetails(updatedCar.getDetails());
            return repository.save(car);
          }).orElseThrow(() -> new CarNotFoundException("Vehicle with id '" + updatedId + "'"));
    } else {
      throw new CarNotFoundException("Please use existing ID");
    }
  }

  /**
   * Deletes a given car by ID
   *
   * @param id the ID number of the car to delete
   */
  public void delete(Long id) throws CarNotFoundException {
    if (repository.existsById(id)) {
      priceClient.delete(id);
      repository.deleteById(id);
    } else {
      throw new CarNotFoundException("Car with id '" + id + "' does not exists.");
    }
  }
}
