package com.udacity.pricing.domain.price;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PriceRepositoryTest {

  private final PriceRepository repository;

  public PriceRepositoryTest(PriceRepository repository) {
    this.repository = repository;
  }
}
