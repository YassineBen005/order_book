package com.lob.order_book.controller;

import com.lob.order_book.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService  analyticsService;
    @GetMapping("/volume")
    public ResponseEntity<List<Object[]>> getVolume(){
        return ResponseEntity.ok(analyticsService.getVolumePerHour());
    }
    @GetMapping("/avg-price")
    public ResponseEntity<List<Object[]>> getAvgPrice(){
        return ResponseEntity.ok(analyticsService.getAvgPricePerHour());
    }
    @GetMapping("/market-depth")
    public ResponseEntity<List<Object[]>> getMarketDepth(){
        return ResponseEntity.ok(analyticsService.getMarketDepth());
    }
    @GetMapping("/spread")
    public ResponseEntity<Double> getSpread(){
        return ResponseEntity.ok(analyticsService.getSpread());
    }
}
