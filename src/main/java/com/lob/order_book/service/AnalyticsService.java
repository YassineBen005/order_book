package com.lob.order_book.service;

import com.lob.order_book.repository.OrderRepository;
import com.lob.order_book.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnalyticsService {
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    OrderBook orderBook;
    public List<Object[]> getVolumePerHour(){
        return tradeRepository.getVolumePerHour();
    }
    public List<Object[]> getAvgPricePerHour(){
        return tradeRepository.getAvgPriceHour();
    }
    @Autowired
    OrderRepository orderRepository;
    public List<Object[]> getMarketDepth(){
        return orderRepository.getMarketDepth();
    }
    public  double getSpread(){
        return orderBook.getSpread();
    }
}
