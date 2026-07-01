package com.lob.order_book.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BookSnapshot {
    private List<Order> bids;
    private List<Order> asks;
}
