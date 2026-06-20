package com.lob.order_book.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private String id;
    private int quantity;
    private double price;
    private OrderSide side;
    private OrderStatus status;
    private LocalDateTime creationTime;
    private int remainingQuantity;

}