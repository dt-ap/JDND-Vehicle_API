package com.udacity.vehicles.client.maps;

import com.udacity.vehicles.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements a class to interface with the Maps Client for location data.
 */
@Component
public class MapsClient {

  private static final Logger log = LoggerFactory.getLogger(MapsClient.class);

  private final WebClient client;
  private final AddressMapper mapper;

  public MapsClient(WebClient.Builder clientBuilder, @Value("${maps.endpoint}") String endpoint, AddressMapper mapper) {
    this.client = clientBuilder.baseUrl(endpoint).build();
    this.mapper = mapper;
  }

  /**
   * Gets an address from the Maps client, given latitude and longitude.
   * @param location An object containing "lat" and "lon" of location
   * @return An updated location including street, city, state and zip,
   *   or an exception message noting the Maps service is down
   */
  public Location get(Location location) {
    try {
      Address address = client
          .get()
          .uri(uriBuilder -> uriBuilder
              .path("/maps")
              .queryParam("lat", location.getLat())
              .queryParam("lon", location.getLon())
              .build()
          )
          .retrieve().bodyToMono(Address.class).block();

      return mapper.toLocation(location, address);
    } catch (Exception e) {
      log.warn("Map service is down");
      return location;
    }
  }
}
