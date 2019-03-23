package com.test2.test2.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test2.test2.model.Price;
import com.test2.test2.model.Product;
import com.test2.test2.service.PriceServiceImpl;
import com.test2.test2.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductHandler {
    @Autowired
    ProductServiceImpl productService;

    @Autowired
    PriceServiceImpl priceService;

    public ResponseEntity<String> addNewProduct(Product product){
        Product addedProduct = productService.add(product);
        if (addedProduct !=null){
            try {
                String response = new ObjectMapper().writeValueAsString(addedProduct);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (JsonProcessingException ex){
                return new ResponseEntity<>("Error when trying to create Json", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
            return new ResponseEntity<>("Product with this name is already exist", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ResponseEntity<String> deleteProduct(String name){
        Optional<Product> deletedProduct = productService.findByName(name);
        if (deletedProduct.isPresent()) {
            productService.deletePerson(deletedProduct.get());
            return new ResponseEntity<>("Product with name " + name + " was deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product with name " + name + " doesn't exist", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> getCurrentPrices(String date){
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date findDate = sdf.parse(sdf.format(new Date()));
            if (date != null) {
                try {
                    findDate = sdf.parse(date);
                    findDate.setHours(3);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            List<Product> products = productService.getAll();
            for (Product p : products) {
                Optional<Price> price = priceService.findPriceForDate(findDate, p.getId());
                if (price.isPresent()) {
                    builder.append(p.getName()).append(" ").append(price.get().getPrice()).append(" ").append(findDate.toString());
                    builder.append("\n");
                }
            }
        }
        catch (Exception ex){
            return new ResponseEntity<>("Error when trying to get prices", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(builder.toString(), HttpStatus.OK);
    }
}
