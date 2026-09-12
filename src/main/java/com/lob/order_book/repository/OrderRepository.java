package com.lob.order_book.repository;

import com.lob.order_book.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,String> {
    @Query("SELECT \n" +
            "    DATE_TRUNC('hour', o.creationTime),\n" +
            "    MAX(CASE WHEN o.side = 'BUY' THEN o.price END) as bestBid," +
            "    MIN(CASE WHEN o.side = 'SELL' THEN o.price END) as bestAsk," +
            "    MIN(CASE WHEN o.side = 'SELL' THEN o.price END) - MAX(CASE WHEN o.side = 'BUY' THEN o.price END) as spread " +
            "FROM OrderEntity o " +
            "WHERE o.status IN ('OPEN', 'PART_FILLED')"+
            "GROUP BY DATE_TRUNC('hour', o.creationTime)")
    List<Object[]> getMarketDepth();
}
