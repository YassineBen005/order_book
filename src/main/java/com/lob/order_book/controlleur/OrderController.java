package com.lob.order_book.controlleur;

import com.lob.order_book.model.*;
import com.lob.order_book.service.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderBook orderBook;
    @PostMapping
    public ResponseEntity<List<Trade>> createOrder(@RequestBody Order order) {
        var trades = orderBook.addOrder(order);
        return ResponseEntity.ok(trades);
    }
    @GetMapping
    public ResponseEntity<BookSnapshot> getOrders(){
        return ResponseEntity.ok(new BookSnapshot(orderBook.getBids(), orderBook.getAsks()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable String id){
        var bool = orderBook.cancelOrder(id);
        return ResponseEntity.ok(bool);
    }

}
