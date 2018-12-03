package se.kth.id1212.conversion.presentation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class CurrencyConversionForm {

  @NotNull(message = "Please choose a conversion")
  @Positive(message = "Please choose a conversion")
  private Long currencyId;

  @NotNull(message = "Please choose an amount")
  @PositiveOrZero(message = "Please choose an amount")
  private Float amount;

  public void setCurrencyId(Long id) {
    this.currencyId = id;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public Long getCurrencyId() {
    return this.currencyId;
  }

  public Float getAmount() {
    return this.amount;
  }
}
