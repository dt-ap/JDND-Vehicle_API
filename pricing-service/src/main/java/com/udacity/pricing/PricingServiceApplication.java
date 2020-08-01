package com.udacity.pricing;

import com.udacity.pricing.domain.price.Currency;
import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Creates a Spring Boot Application to run the Pricing Service.
 */
@SpringBootApplication
@EnableEurekaClient
public class PricingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PricingServiceApplication.class, args);
  }

  @Bean
  CommandLineRunner initDatabase(PriceRepository repository) {
    return args -> {
      repository.saveAll(List.of(
          new Price(1L, Currency.USD, BigDecimal.valueOf(50000)),
          new Price(2L, Currency.USD, BigDecimal.valueOf(29400)),
          new Price(3L, Currency.USD, BigDecimal.valueOf(31999)),
          new Price(4L, Currency.USD, BigDecimal.valueOf(40000)),
          new Price(5L, Currency.USD, BigDecimal.valueOf(30500))
      ));
    };
  }
}
