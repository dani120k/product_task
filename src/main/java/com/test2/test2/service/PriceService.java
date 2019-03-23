package com.test2.test2.service;

import com.test2.test2.model.Price;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PriceService extends CrudRepository<Price, Long> {
    @Query(value = "SELECT p FROM Price p " +
            "WHERE p.productId = :id AND (p.start_date IS NULL OR p.start_date <= :date) AND (p.end_date IS NULL OR p.end_date > :date)")
    Optional<Price> findPriceForDate(@Param("date") Date date, @Param("id") Long id);

    List<Price> findByProductId(Long id);
}
