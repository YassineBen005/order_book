package com.lob.order_book.repository;

import com.lob.order_book.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository  extends JpaRepository<TradeEntity,String> {
}
