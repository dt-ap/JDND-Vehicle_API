package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.*;
import com.udacity.pricing.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PricingControllerTest {
  private static final List<Price> PRICES = List.of(
      new Price(1L, Currency.USD, BigDecimal.valueOf(35000)),
      new Price(2L, Currency.EUR, BigDecimal.valueOf(26500))
  );

  private PricingController controller;
  private final PriceRepository repository = mock(PriceRepository.class);

  @BeforeEach
  public void setup() {
    controller = new PricingController(new PriceService(repository), new PriceMapperImpl());
    when(repository.findByVehicleId(anyLong())).thenAnswer((inv) -> {
      var id = (Long) inv.getArgument(0);
      return PRICES.stream().filter(i -> i.getVehicleId().equals(id)).findFirst();
    });
    doThrow(RuntimeException.class).when(repository).deleteById(argThat(id -> PRICES.stream().noneMatch(p -> p.getVehicleId().equals(id))));
  }

  @Test
  public void findPriceByVehicleID() {
    var price = controller.get(1L);

    assertNotNull(price);
    assertEquals(Currency.USD, price.getCurrency());
    assertEquals(BigDecimal.valueOf(35000), price.getValue());
  }

  @Test
  public void findPriceByFalseVehicleID() {
    var exc = assertThrows(ResponseStatusException.class, () -> {
      controller.get(3L);
    });

    assertEquals(HttpStatus.NOT_FOUND, exc.getStatus());
    assertEquals("Vehicle with id: '3' not found", exc.getReason());
  }

  @Test
  public void createOrUpdateVehicle() {
    var dto = new PriceDTO(3L, Currency.USD, BigDecimal.valueOf(38500));
    var expectedPrice = new Price(3L, Currency.USD, BigDecimal.valueOf(38500));
    when(repository.save(argThat(p -> p.getVehicleId().equals(3L)))).thenReturn(expectedPrice);

    var price = controller.post(dto);

    assertNotNull(price);
    assertEquals(dto.getCurrency(), expectedPrice.getCurrency());
    assertEquals(dto.getValue(), expectedPrice.getValue());
  }

  @Test
  public void deletePriceByVehicleID() {
    assertDoesNotThrow(() -> controller.delete(1L));
  }

  @Test
  public void deletePriceByFalseVehicleID() {
    var exc = assertThrows(ResponseStatusException.class, () -> {
      controller.delete(3L);
    });

    assertEquals(HttpStatus.NOT_FOUND, exc.getStatus());
    assertEquals("Vehicle with id: '3' does not exists", exc.getReason());
  }
}
