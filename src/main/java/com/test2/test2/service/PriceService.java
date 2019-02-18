package com.test2.test2.service;

import com.test2.test2.model.Price;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Date;
import java.util.List;

public interface PriceService extends CrudRepository<Price, Long> {
    @Query(value = "SELECT * " +
            "FROM price " +
            "WHERE product_id = (?2) AND ((start_date <= (?1) AND end_date is NULL) OR (end_date > (?1) AND  start_date is NULL) OR (start_date <=(?1) AND end_date >(?1)) OR (start_date is NULL AND end_date is NULL)) " +
            "LIMIT 1", nativeQuery = true)
    Price findPriceForDate(Date date, Long id);

    @Query(value = "SELECT * " +
            "FROM price " +
            "WHERE product_id = (?1)", nativeQuery = true)
    List<Price> getByProductId(Long id);
}
