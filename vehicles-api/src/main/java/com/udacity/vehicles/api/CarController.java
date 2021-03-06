package com.udacity.vehicles.api;


import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarFoundException;
import com.udacity.vehicles.service.CarNotFoundException;
import com.udacity.vehicles.service.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implements a REST-based controller for the Vehicles API.
 */
@RestController
@RequestMapping("/cars")
class CarController {

  private final CarService service;
  private final CarModelAssembler assembler;

  CarController(CarService carService, CarModelAssembler assembler) {
    this.service = carService;
    this.assembler = assembler;
  }

  /**
   * Creates a list to store any vehicles.
   * @return list of vehicles
   */
  @GetMapping
  CollectionModel<EntityModel<Car>> list() {
    var models = service.readAll().stream().map(assembler::toModel)
        .collect(Collectors.toList());
    return CollectionModel.of(models,
        linkTo(methodOn(CarController.class).list()).withSelfRel());
  }

  /**
   * Gets information of a specific car by ID.
   * @param id the id number of the given vehicle
   * @return all information for the requested vehicle
   */
  @GetMapping("/{id}")
  EntityModel<Car> get(@PathVariable Long id) throws CarNotFoundException {
    var car = service.read(id);
    return assembler.toModel(car);
  }

  /**
   * Posts information to create a new vehicle in the system.
   * @param car A new vehicle to add to the system.
   * @return response that the new vehicle was added to the system
   * @throws CarFoundException if car already exists.
   * @throws NoSuchElementException If no element found.
   */
  @PostMapping
  ResponseEntity<?> post(@Valid @RequestBody Car car) throws CarFoundException, NoSuchElementException {
    var saved = service.create(car);
    var model = assembler.toModel(saved);
    return ResponseEntity.created(model.getLink("self").get().toUri()).body(model);
  }

  /**
   * Updates the information of a vehicle in the system.
   * @param id The ID number for which to update vehicle information.
   * @param car The updated information about the related vehicle.
   * @return response that the vehicle was updated in the system
   */
  @PutMapping("/{id}")
  ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) throws CarNotFoundException {
    car.setId(id);
    var updated = service.update(car);
    var model = assembler.toModel(updated);
    return ResponseEntity.ok(model);
  }

  /**
   * Removes a vehicle from the system.
   * @param id The ID number of the vehicle to remove.
   * @return response that the related vehicle is no longer in the system
   */
  @DeleteMapping("/{id}")
  ResponseEntity<?> delete(@PathVariable Long id) throws CarNotFoundException {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
