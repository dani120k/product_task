package com.test2.test2.rest;

import com.test2.test2.handlers.PriceHandler;
import com.test2.test2.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/price")
public class PriceController {
    @Autowired
    private PriceHandler priceHandler;

    @RequestMapping(method = RequestMethod.GET, value = "/add/{name}")
    public ResponseEntity<String> createPrice(@PathVariable String name, @RequestParam("price") Long price, @RequestParam(required = false) String start_date, @RequestParam(required = false) String end_date){
        return priceHandler.addNew(name, price, start_date, end_date);
    }
}
