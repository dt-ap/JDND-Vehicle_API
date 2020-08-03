package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient {

  private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

  private final WebClient client;

  public PriceClient(WebClient.Builder clientBuilder, @Value("${pricing.endpoint}") String endpoint) {
    this.client = clientBuilder.baseUrl(endpoint).build();
  }

  // In a real-world application we'll want to add some resilience
  // to this method with retries/CB/failover capabilities
  // We may also want to cache the results so we don't need to
  // do a request every time

  /**
   * Gets a vehicle price from the pricing client, given vehicle ID.
   *
   * @param vehicleId ID number of the vehicle for which to get the price
   * @return Price of the requested vehicle,
   * error message that the vehicle ID is invalid, or null when the
   * service is down.
   */
  public Price get(Long vehicleId) {
    try {
      return client
          .get()
          .uri("prices/" + vehicleId)
          .retrieve().bodyToMono(Price.class).block();
    } catch (Exception e) {
      log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
    }
    return null;
  }

  public Price post(Price newPrice) {
    try {
      return client
          .post()
          .uri("prices/")
          .body(BodyInserters.fromValue(newPrice))
          .retrieve().bodyToMono(Price.class).block();

    } catch (Exception e) {
      log.error("Unexpected error creating price for vehicle {}", newPrice.getVehicleId(), e);
    }
    return null;
  }

  public Long delete(Long vehicleId) {
    try {
      client.delete().uri("prices/" + vehicleId);
      return vehicleId;
    } catch (Exception e) {
      log.error("Unexpected error deleting price for vehicle {}", vehicleId, e);
    }
    return null;
  }
}
