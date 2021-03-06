package com.test2.test2.rest;

import com.test2.test2.handlers.PriceHandler;
import com.test2.test2.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/price")
public class PriceController {
    @Autowired
    private PriceHandler priceHandler;


    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    public String getAllPrices(){
        return priceHandler.getAllPrices();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public String createPrice(@RequestBody Price price, @RequestParam String productName){
        return priceHandler.addNew(price, productName);

    }
}
