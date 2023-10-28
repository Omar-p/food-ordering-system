package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

  private static final String UTC = "UTC";

  @Override
  public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
    validateRestaurant(restaurant);
    setOrderProductInformation(order, restaurant);
    order.validateOrder();
    order.initializeOrderItems();

    log.info("Order created successfully. OrderId: {}", order.getId().getValue());

    return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public OrderPaidEvent payOrder(Order order) {
    order.pay();
    log.info("Order paid successfully. OrderId: {}", order.getId().getValue());
    return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public void approveOrder(Order order) {
    order.approve();
    // u may fire an event which will be consumed by client to continue to the delivery process.
    log.info("Order approved successfully. OrderId: {}", order.getId().getValue());
  }

  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
    order.initCancel(failureMessages);
    log.info("Order payment cancelled successfully. OrderId: {}", order.getId().getValue());
    return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public void cancelOrder(Order order, List<String> failureMessages) {
    order.cancel(failureMessages);
    log.info("Order cancelled successfully. OrderId: {}", order.getId().getValue());
  }

  private void setOrderProductInformation(Order order, Restaurant restaurant) {
    order.getItems().forEach(orderItem -> {
      restaurant.getProducts().forEach(restaurantProduct -> {
        if (orderItem.getProduct().equals(restaurantProduct)) {
          orderItem.getProduct().updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
        }
      });
    });
  }

  private void validateRestaurant(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException("Restaurant is not active. RestaurantId: " + restaurant.getId().getValue());
    }
  }

}
