package com.test2.test2.rest;

import com.test2.test2.handlers.ProductHandler;
import com.test2.test2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductHandler productHandler;

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public String createProduct(@RequestBody Product product){
        return productHandler.addNewProduct(product);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public String deleteProduct(@RequestParam String name){
        return productHandler.deleteProduct(name);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllPrices")
    public String getAllPricesOnCurrentDate(@RequestParam(required = false) String date) {
        return productHandler.getCurrentPrices(date);
    }



}
