package com.udacity.vehicles;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Launches a Spring Boot application for the Vehicles API,
 * initializes the car manufacturers in the database,
 * and launches web clients to communicate with maps and pricing.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
public class VehiclesApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(VehiclesApiApplication.class, args);
  }

  /**
   * Initializes the car manufacturers available to the Vehicle API.
   *
   * @param carRepo          where the car information persists.
   * @param manufacturerRepo where the manufacturer information persists.
   * @return lambda of things to persists.
   */
  @Bean
  CommandLineRunner initDatabase(CarRepository carRepo, ManufacturerRepository manufacturerRepo) {
    return args -> {
      var ms = manufacturerRepo.saveAll(List.of(
          new Manufacturer(100, "Audi"),
          new Manufacturer(101, "Ford"),
          new Manufacturer(102, "BMW")
      ));

      carRepo.saveAll(List.of(
          new Car(1L, Condition.NEW,
              new Details("Sedan", "Audi A4", ms.get(0), 4,
                  "Petrol", "V6", 18100, 2015, 2016, "Black"),
              new Location(0d, 0d), null
          ),
          new Car(2L, Condition.USED,
              new Details("SUV", "Ford Everest", ms.get(1), 4,
                  "Diesel", "3.2 L Duratorq", 14000, 2013, 2013, "White"),
              new Location(0d, 0d), null
          ),
          new Car(3L, Condition.USED,
              new Details("SUV", "Audi Q7", ms.get(0), 5,
                  "Diesel", "3.0 TDI Ultra", 16850, 2015, 2014, "Blue"),
              new Location(0d, 0d), null
          ),
          new Car(4L, Condition.NEW,
              new Details("SUV", "BMW X1", ms.get(2), 5,
                  "Petrol", "1.5 L B38", 13999, 2015, 2014, "Silver"),
              new Location(0d, 0d), null
          ),
          new Car(5L, Condition.USED,
              new Details("Sedan", "BMW 5 Series", ms.get(2), 4,
                  "Petrol", "2.5–3.0 L N52/N53", 17499, 2010, 2009, "Silver"),
              new Location(0d, 0d), null
          )
      ));
    };
  }

  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

}
