package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.va.TrackingId;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
  private final CustomerId customerId;
  private final ResturantId resturantId;
  private final StreetAddress deliveryAddress;
  private final Money price;
  private final List<OrderItem> items;
  private TrackingId trackingId;
  private OrderStatus orderStatus;
  private List<String> failureMessages;

  public void initializeOrder() {
    setId(new OrderId(UUID.randomUUID()));
    this.trackingId = new TrackingId(UUID.randomUUID());
    this.orderStatus = OrderStatus.PENDING;
    initializeOrderItems();
  }

  public void initializeOrderItems() {
    long itemId = 1;
    for (OrderItem item : items) {
      item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
    }
  }

  public void validateOrder() {
    validateInitialOrder();
    validateTotalPrice();
    validateItemsPrice();
  }

  private void validateItemsPrice() {
    var orderItemTotal = this.items.stream()
        .map(orderItem -> {
          validateItemPrice(orderItem);
          return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

    if (!orderItemTotal.equals(this.price)) {
      throw new OrderDomainException("Total price: %.2f$ is not equal to Order item total: %.2f$"
          .formatted(this.price.getAmount(), orderItemTotal.getAmount()));
    }
  }

  private void validateItemPrice(OrderItem orderItem) {
    if (!orderItem.isPriceValid()) {
      throw new OrderDomainException("Order item price is %.2f$ is not valid for product price: %.2f$"
          .formatted(orderItem.getPrice().getAmount(), orderItem.getProduct().getPrice().getAmount()));
    }
  }

  private void validateTotalPrice() {
    if (this.price == null || !this.price.isGreaterThanZero()) {
      throw new OrderDomainException("Total price must be greater than zero");
    }
  }

  private void validateInitialOrder() {
    if (orderStatus != null || super.getId() != null) {
      throw new OrderDomainException("Order is not in correct state for initialization");
    }
  }

  private Order(Builder builder) {
    super.setId(builder.id);
    customerId = builder.customerId;
    resturantId = builder.resturantId;
    deliveryAddress = builder.deliveryAddress;
    price = builder.price;
    items = builder.items;
    orderStatus = builder.orderStatus;
    trackingId = builder.trackingId;
    failureMessages = builder.failureMessages;
  }


  public CustomerId getCustomerId() {
    return customerId;
  }

  public ResturantId getResturantId() {
    return resturantId;
  }

  public StreetAddress getDeliveryAddress() {
    return deliveryAddress;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public Money getPrice() {
    return price;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public TrackingId getTrackingId() {
    return trackingId;
  }


  public List<String> getFailureMessages() {
    return failureMessages;
  }

  public static final class Builder {
    private OrderId id;
    private CustomerId customerId;
    private ResturantId resturantId;
    private StreetAddress deliveryAddress;
    private Money price;
    private List<OrderItem> items;
    private OrderStatus orderStatus;
    private TrackingId trackingId;
    private List<String> failureMessages;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder id(OrderId val) {
      id = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder resturantId(ResturantId val) {
      resturantId = val;
      return this;
    }

    public Builder deliveryAddress(StreetAddress val) {
      deliveryAddress = val;
      return this;
    }

    public Builder orderStatus(OrderStatus val) {
      orderStatus = val;
      return this;
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Builder items(List<OrderItem> val) {
      items = val;
      return this;
    }

    public Builder trackingId(TrackingId val) {
      trackingId = val;
      return this;
    }

    public Builder failureMessages(List<String> val) {
      failureMessages = val;
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }
}
