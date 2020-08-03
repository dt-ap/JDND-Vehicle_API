package com.udacity.vehicles.api;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarNotFoundException;
import com.udacity.vehicles.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the CarController class.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private JacksonTester<Car> json;

  @MockBean
  private CarService carService;

  @MockBean
  private PriceClient priceClient;

  @MockBean
  private MapsClient mapsClient;

  /**
   * Creates pre-requisites for testing, such as an example car.
   */
  @BeforeEach
  public void setup() {
    Car car = getCar();
    car.setId(1L);
    given(carService.create(any())).willReturn(car);
    given(carService.read(eq(car.getId()))).willReturn(car);
    given(carService.read(not(eq(car.getId())))).willThrow(CarNotFoundException.class);
    given(carService.readAll()).willReturn(Collections.singletonList(car));
    willThrow(CarNotFoundException.class).given(carService).delete(not(eq(car.getId())));
  }

  /**
   * Tests for successful creation of new car in the system
   *
   * @throws Exception when car creation fails in the system
   */
  @Test
  public void createCar() throws Exception {
    Car car = getCar();
    mvc.perform(
        post(new URI("/cars"))
            .content(json.write(car).getJson())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  /**
   * Tests if the read operation appropriately returns a list of vehicles.
   *
   * @throws Exception if the read operation of the vehicle list fails
   */
  @Test
  public void listCars() throws Exception {
    mvc.perform(get("/cars").accept(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
  }

  /**
   * Tests the read operation for a single car by ID.
   *
   * @throws Exception if the read operation for a single car fails
   */
  @Test
  public void findCar() throws Exception {
    var car = getCar();
    car.setId(1L);
    mvc.perform(get(new URI("/cars/" + car.getId())).accept(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
  }

  /**
   * Tests the deletion of a single car by ID.
   *
   * @throws Exception if the delete operation of a vehicle fails
   */
  @Test
  public void deleteCar() throws Exception {
    var car = getCar();
    car.setId(1L);
    mvc.perform(delete(new URI("/cars/" + car.getId()))).andExpect(status().isNoContent());
  }

  /**
   * Tests the updates of a single car by ID.
   *
   * @throws Exception if the update operation fails
   */
  @Test
  public void updateCar() throws Exception {
    var car = getCar();
    car.setId(1L);
    car.getDetails().setModel("Impala II");
    car.setCondition(Condition.NEW);
    given(carService.update(argThat(c -> c.getId().equals(car.getId())))).willReturn(car);

    mvc.perform(
        put(new URI("/cars/" + car.getId()))
            .content(json.write(car).getJson())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.details.model").value(car.getDetails().getModel()))
        .andExpect(jsonPath("$.condition").value(Condition.NEW.toString()));


  }

  /**
   * Tests not found read operation for a single car using false ID.
   *
   * @throws Exception if the read operation for a single car fails
   */
  @Test
  public void findCarNotFound() throws Exception {
    mvc.perform(get(new URI("/cars/" + 2L)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
  }

  /**
   * Tests the updates of a single car using false ID.
   *
   * @throws Exception if the update operation fails
   */
  @Test
  public void updateCarNotFound() throws Exception {
    var car = getCar();
    car.setId(1L);
    car.setCondition(Condition.USED);

    given(carService.update(argThat(c -> !c.getId().equals(car.getId())))).willThrow(CarNotFoundException.class);

    mvc.perform(put(new URI("/cars/" + 2L))
        .content(json.write(car).getJson())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
  }

  /**
   * Tests the deletion of a single car using false ID.
   *
   * @throws Exception if the delete operation of a vehicle fails
   */
  @Test
  public void deleteCarNotFound() throws Exception {
    mvc.perform(delete(new URI("/cars/" + 2L))).andExpect(status().isNotFound());
  }

  /**
   * Creates an example Car object for use in testing.
   *
   * @return an example Car object
   */
  private Car getCar() {
    Car car = new Car();
    car.setLocation(new Location(40.730610, -73.935242));
    Details details = new Details();
    Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
    details.setManufacturer(manufacturer);
    details.setModel("Impala");
    details.setMileage(32280);
    details.setExternalColor("white");
    details.setBody("sedan");
    details.setEngine("3.6L V6");
    details.setFuelType("Gasoline");
    details.setModelYear(2018);
    details.setProductionYear(2018);
    details.setNumberOfDoors(4);
    car.setDetails(details);
    car.setCondition(Condition.USED);
    return car;
  }
}