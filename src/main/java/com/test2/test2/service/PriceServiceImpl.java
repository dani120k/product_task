package com.test2.test2.service;

import com.test2.test2.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PriceServiceImpl {
    @Autowired
    private PriceService repository;

    public List<Price> getAll() {
        return (List<Price>)repository.findAll();
    }

    public Price add(Price price){
        return repository.save(price);
    }

    public void deletePrice(Price price){
        repository.delete(price);
    }

    public Optional<Price> findPriceForDate(Date date, Long id){return repository.findPriceForDate(date, id);}

    public List<Price> getByProductId(Long id){return repository.findByProductId(id);
    }

}