package com.lob.order_book.entity;

import com.lob.order_book.model.OrderSide;
import com.lob.order_book.model.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class OrderEntity {
    @Id
    private String id;
    private int quantity;
    private double price;
    @Enumerated(EnumType.STRING)
    private OrderSide side;/*BUY or SELL*/
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime creationTime;
    private int remainingQuantity;
}
