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

}
