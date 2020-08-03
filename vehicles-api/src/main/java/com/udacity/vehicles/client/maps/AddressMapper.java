package com.udacity.vehicles.client.maps;

import com.udacity.vehicles.domain.Location;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface AddressMapper {
  Location toLocation(@MappingTarget Location location, Address address);
}
