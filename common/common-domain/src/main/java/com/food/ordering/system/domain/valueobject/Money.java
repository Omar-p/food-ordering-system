package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {
  private final BigDecimal amount;

  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money(BigDecimal amount) {
    this.amount = amount;
  }

  public boolean isGreaterThanZero() {
    return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
  }

  public boolean isGreaterThan(Money other) {
    return this.amount != null && this.amount.compareTo(other.amount) > 0;
  }

  public Money add(Money other) {
    return new Money(setScale(this.amount.add(other.amount)));
  }

  public Money subtract(Money other) {
    return new Money(setScale(this.amount.subtract(other.amount)));
  }

  public Money multiply(int multiplier) {
    return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Money money)) return false;

    return getAmount() != null ? getAmount().equals(money.getAmount()) : money.getAmount() == null;
  }

  @Override
  public int hashCode() {
    return getAmount() != null ? getAmount().hashCode() : 0;
  }

  private BigDecimal setScale(BigDecimal input) {
    return input.setScale(2, RoundingMode.HALF_UP);
  }
}
