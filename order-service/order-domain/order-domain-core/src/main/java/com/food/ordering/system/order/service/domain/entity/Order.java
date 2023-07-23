package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.va.TrackingId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;

import java.util.List;

public class Order extends AggregateRoot<OrderId> {
  private final CustomerId customerId;
  private final ResturantId resturantId;
  private final StreetAddress deliveryAddress;
  private final OrderStatus orderStatus;
  private final Money price;
  private final List<OrderItem> items;
  private TrackingId trackingId;
  private OrderStatus status;
  private List<String> failureMessages;

  private Order(Builder builder) {
    super.setId(builder.id);
    customerId = builder.customerId;
    resturantId = builder.resturantId;
    deliveryAddress = builder.deliveryAddress;
    orderStatus = builder.orderStatus;
    price = builder.price;
    items = builder.items;
    trackingId = builder.trackingId;
    status = builder.status;
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

  public OrderStatus getStatus() {
    return status;
  }

  public List<String> getFailureMessages() {
    return failureMessages;
  }

  public static final class Builder {
    private OrderId id;
    private CustomerId customerId;
    private ResturantId resturantId;
    private StreetAddress deliveryAddress;
    private OrderStatus orderStatus;
    private Money price;
    private List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus status;
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

    public Builder status(OrderStatus val) {
      status = val;
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
