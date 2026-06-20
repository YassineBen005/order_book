package com.lob.order_book.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Trade {
    private String buyOrderId;
    private String sellOrderId;
    private LocalDateTime tradeTime;
    private double price;
    private int quantity;
}
