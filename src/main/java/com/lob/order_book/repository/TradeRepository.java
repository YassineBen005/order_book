package com.lob.order_book.repository;

import com.lob.order_book.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradeRepository  extends JpaRepository<TradeEntity,String> {
    @Query("SELECT DATE_TRUNC('hour', t.tradeTime), SUM(t.quantity) FROM TradeEntity t GROUP BY DATE_TRUNC('hour', t.tradeTime)")
    List<Object[]> getVolumePerHour();
    @Query("SELECT DATE_TRUNC('hour',t.tradeTime),AVG(t.price) FROM TradeEntity t GROUP BY DATE_TRUNC('hour',t.tradeTime)")
    List<Object[]> getAvgPriceHour();

}
