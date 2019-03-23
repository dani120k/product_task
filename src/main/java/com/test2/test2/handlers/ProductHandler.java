package com.test2.test2.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test2.test2.model.Price;
import com.test2.test2.model.Product;
import com.test2.test2.service.PriceServiceImpl;
import com.test2.test2.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String addNewProduct(Product product){
        Product addedProduct = productService.add(product);
        if (addedProduct !=null){
            try {
                String response = new ObjectMapper().writeValueAsString(addedProduct);
                return response;
            } catch (JsonProcessingException ex){
                return "Problem when trying to create Json";
            }
        }
        else
        {
            try {
                String response = new ObjectMapper().writeValueAsString("Product with this name is already exist");
                return response;
            } catch (JsonProcessingException ex){
                return "Problem when trying to create Json";
            }
        }
    }

    public String deleteProduct(String name){
        Optional<Product> deletedProduct = productService.findByName(name);
        if (deletedProduct.isPresent()) {
            productService.deletePerson(deletedProduct.get());
            return "Product with name " + name + " was deleted";
        }
        else
            return "Product with name " + name + " doesnt exist";
    }

    public String getCurrentPrices(String date){
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
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
