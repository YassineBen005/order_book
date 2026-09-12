package com.lob.order_book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class TradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String buyOrderId; /*Order BUY id*/
    private String sellOrderId;/*Order SELL id*/
    private LocalDateTime tradeTime;
    private double price;
    private int quantity;
}
