package com.test2.test2.service;

import com.test2.test2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductService repository;

    public List<Product> getAll() {
        return (List<Product>)repository.findAll();
    }

    public Product add(Product product){
            return repository.save(product);
    }

    public void deletePerson(Product product){
        repository.delete(product);
    }

    public Optional<Product> findByName(String name){return repository.findOneByName(name);}

}