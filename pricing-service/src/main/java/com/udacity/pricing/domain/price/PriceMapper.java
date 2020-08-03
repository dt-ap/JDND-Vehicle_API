package com.udacity.pricing.domain.price;

import org.mapstruct.Mapper;

@Mapper
public interface PriceMapper {
  Price toEntity(PriceDTO dto);

  PriceDTO toDto(Price price);
}
