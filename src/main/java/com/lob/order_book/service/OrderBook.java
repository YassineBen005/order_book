package com.lob.order_book.service;

import com.lob.order_book.entity.OrderEntity;
import com.lob.order_book.entity.TradeEntity;
import com.lob.order_book.model.Order;
import com.lob.order_book.model.OrderSide;
import com.lob.order_book.model.OrderStatus;
import com.lob.order_book.model.Trade;
import com.lob.order_book.repository.OrderRepository;
import com.lob.order_book.repository.TradeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class OrderBook {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    TradeRepository tradeRepository;
    /* Using queues to stock bids and asks:
   - bids sorted by descending price (highest buyer first)
   - asks sorted by ascending price (lowest seller first) */
    private final PriorityBlockingQueue<Order> bids = new PriorityBlockingQueue<>(100, Comparator.comparingDouble(Order::getPrice).reversed());
    private final PriorityBlockingQueue<Order> asks = new PriorityBlockingQueue<>(100, Comparator.comparingDouble(Order::getPrice));
    /*a Hash Map id:order*/
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
        orderRepository.save(toOrderEntity(order));
        /*each order added is matched directly with the corresponding queue*/
        if (order.getSide() == OrderSide.BUY) {
            return match(order, asks);
        } else {
            return match(order, bids);
        }
    }

    private List<Trade> match(Order order, PriorityBlockingQueue<Order> queue) {
        List<Trade> trades = new ArrayList<>();
        while (order.getRemainingQuantity() > 0 && !queue.isEmpty()) {
            /*tests if there is a price match between order and the queue's head*/
            boolean priceMatcher;
            //buyer needs to bid over the seller ask
            if (order.getSide() == OrderSide.BUY) {
                // A buy order matches if its price is greater than or equal to the best ask
                priceMatcher = queue.peek().getPrice() <= order.getPrice();
            } else{
                // A sell order matches if its price is less than or equal to the best bid
                priceMatcher = queue.peek().getPrice() >= order.getPrice();
            }
            if (!priceMatcher) break;
            Order soldOrder = queue.poll();
            Trade trade = new Trade();
            if (order.getSide() == OrderSide.BUY) {
                trade.setBuyOrderId(order.getId());
                trade.setSellOrderId(soldOrder.getId());
            } else {
                trade.setBuyOrderId(soldOrder.getId());
                trade.setSellOrderId(order.getId());
            }
            //Trade's price is the soldOrder's price
            trade.setPrice(soldOrder.getPrice());
            trade.setQuantity(Math.min(soldOrder.getRemainingQuantity(), order.getRemainingQuantity()));
            trade.setTradeTime(LocalDateTime.now());
            trades.add(trade);
            tradeRepository.save(toTradeEntity(trade));
            order.setRemainingQuantity(order.getRemainingQuantity() - trade.getQuantity());
            soldOrder.setRemainingQuantity(soldOrder.getRemainingQuantity() - trade.getQuantity());
            if (soldOrder.getRemainingQuantity() > 0) {
                soldOrder.setStatus(OrderStatus.PART_FILLED);
                queue.add(soldOrder);
                orderRepository.save(toOrderEntity(soldOrder));
            }
            if (soldOrder.getRemainingQuantity() == 0) {
                soldOrder.setStatus(OrderStatus.FILLED);
                orderRepository.save(toOrderEntity(soldOrder));
            }
        }
        /* if order still not completely filled we place it in the queue*/
        if (order.getRemainingQuantity() > 0) {
            order.setStatus(OrderStatus.PART_FILLED);
            orderRepository.save(toOrderEntity(order));
            if (order.getSide() == OrderSide.BUY) {
                bids.add(order);
            } else {
                asks.add(order);
            }
        }else {
            order.setStatus(OrderStatus.FILLED);
            orderRepository.save(toOrderEntity(order));
        }
        return trades;
    }

    public boolean cancelOrder(String id) {
        if (!ordersId.containsKey(id) || ordersId.get(id).getStatus() == OrderStatus.FILLED) {
            return false;
        } else {
            if (ordersId.get(id).getSide() == OrderSide.BUY) {
                bids.remove(ordersId.get(id));
            } else {
                asks.remove(ordersId.get(id));
            }
            ordersId.get(id).setStatus(OrderStatus.CANCELED);
            orderRepository.save(toOrderEntity(ordersId.get(id)));
            ordersId.remove(id);
            return true;
        }
    }
    private OrderEntity toOrderEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setPrice(order.getPrice());
        entity.setQuantity(order.getQuantity());
        entity.setSide(order.getSide());
        entity.setStatus(order.getStatus());
        entity.setCreationTime(order.getCreationTime());
        entity.setRemainingQuantity(order.getRemainingQuantity());
        return entity;
    }
    private TradeEntity toTradeEntity(Trade trade){
        TradeEntity entity = new TradeEntity();
        entity.setBuyOrderId(trade.getBuyOrderId());
        entity.setSellOrderId(trade.getSellOrderId());
        entity.setPrice(trade.getPrice());
        entity.setQuantity(trade.getQuantity());
        entity.setTradeTime(trade.getTradeTime());
        return entity;
    }

    //get the market spread
    public double getSpread(){
        if (!bids.isEmpty() && !asks.isEmpty()){
            Order bestBid=bids.peek();
            Order bestAsk=asks.peek();
            return bestAsk.getPrice()-bestBid.getPrice();
        }else{
            throw new IllegalStateException("Order book is empty, no spread available");
        }
    }
}