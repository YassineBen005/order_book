package com.lob.order_book.service;

import com.lob.order_book.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnalyticsService {
    @Autowired
    TradeRepository tradeRepository;
    public List<Object[]> getVolumePerHour(){
        return tradeRepository.getVolumePerHour();
    }
}
