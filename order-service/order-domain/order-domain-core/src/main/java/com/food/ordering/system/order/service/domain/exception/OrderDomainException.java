package com.food.ordering.system.order.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class OrderDomainException extends DomainException {
  public OrderDomainException(String orderIsAlreadyInitialized) {
    super(orderIsAlreadyInitialized);
  }
}
