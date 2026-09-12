package com.lob.order_book.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
/*Pour simplifier le corps de la requête Get*/
@Data
@AllArgsConstructor
public class BookSnapshot {
    private List<Order> bids;
    private List<Order> asks;
}
