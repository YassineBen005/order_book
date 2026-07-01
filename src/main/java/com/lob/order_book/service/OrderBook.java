package com.lob.order_book.service;

import com.lob.order_book.model.Order;
import com.lob.order_book.model.OrderSide;
import com.lob.order_book.model.OrderStatus;
import com.lob.order_book.model.Trade;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class OrderBook {
    private final PriorityBlockingQueue<Order> bids = new PriorityBlockingQueue<>(100, Comparator.comparingDouble(Order::getPrice).reversed());
    private final PriorityBlockingQueue<Order> asks = new PriorityBlockingQueue<>(100, Comparator.comparingDouble(Order::getPrice));
    private final ConcurrentHashMap<String, Order> ordersId = new ConcurrentHashMap<>();
    public List<Order> getBids() {
        return new ArrayList<>(bids);
    }
    public List<Order> getAsks() {
        return new ArrayList<>(asks);
    }
    public List<Trade> addOrder(Order order) {
        order.setId(UUID.randomUUID().toString());
        order.setRemainingQuantity(order.getQuantity());
        order.setStatus(OrderStatus.OPEN);
        order.setCreationTime(LocalDateTime.now());
        ordersId.put(order.getId(), order);
        if (order.getSide() == OrderSide.BUY) {
            return match(order, asks);
        } else {
            return match(order, bids);
        }
    }

    private List<Trade> match(Order order, PriorityBlockingQueue<Order> queue) {
        List<Trade> trades = new ArrayList<>();
        while (order.getRemainingQuantity() > 0 && !queue.isEmpty()) {
            Boolean priceMatcher = false;
            if (!queue.isEmpty() && order.getSide() == OrderSide.BUY) {

                priceMatcher = queue.peek().getPrice() <= order.getPrice();
            } else {
                priceMatcher = queue.peek().getPrice() >= order.getPrice();
            }
            if (!priceMatcher) break;
            Order soldOrder = queue.poll();
            Trade trade = new Trade();
            trade.setBuyOrderId(order.getId());
            trade.setSellOrderId(soldOrder.getId());
            trade.setPrice(soldOrder.getPrice());
            trade.setQuantity(Math.min(soldOrder.getRemainingQuantity(), order.getRemainingQuantity()));
            trade.setTradeTime(LocalDateTime.now());
            trades.add(trade);
            order.setRemainingQuantity(order.getRemainingQuantity() - trade.getQuantity());
            soldOrder.setRemainingQuantity(soldOrder.getRemainingQuantity() - trade.getQuantity());
            if (soldOrder.getRemainingQuantity() > 0) {
                queue.add(soldOrder);
            }
        }
        /* if order still not completely filled we place it in the queue*/
        if (order.getRemainingQuantity() > 0) {
            if (order.getSide() == OrderSide.BUY) {
                bids.add(order);
            } else {
                asks.add(order);
            }
        }
        return trades;
    }

    public Boolean cancelOrder(String id) {
        if (!ordersId.containsKey(id) || ordersId.get(id).getStatus() == OrderStatus.FILLED) {
            return false;
        } else {
            if (ordersId.get(id).getSide() == OrderSide.BUY) {
                bids.remove(ordersId.get(id));
            } else {
                asks.remove(ordersId.get(id));
            }
            ordersId.get(id).setStatus(OrderStatus.CANCELED);
            ordersId.remove(id);
            return true;
        }
    }
}