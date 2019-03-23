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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PriceHandler {
    @Autowired
    private PriceServiceImpl priceService;

    @Autowired
    private ProductServiceImpl productService;

    public ResponseEntity<String> addNew(String name, Long price, String start_date, String end_date){
        Optional<Product> product = productService.findByName(name);
        if (!product.isPresent())
            return new ResponseEntity<>("Product with this name doesn't exist", HttpStatus.NOT_FOUND);

        Price addPrice = new Price();
        addPrice.setPrice(price);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (start_date!=null){
                Date sd = sdf.parse(start_date);
                sd.setHours(3);
                addPrice.setStart_date(sdf.parse(start_date));
            }

            if (end_date!=null){
                Date sd = sdf.parse(end_date);
                sd.setHours(3);
                addPrice.setEnd_date(sdf.parse(end_date));
            }
        } catch (ParseException ex){
            return new ResponseEntity<>("Unable to parse input date", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        addPrice.setProductId(product.get().getId());
        refreshPrices(addPrice);

        try {
            priceService.add(addPrice);
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(addPrice), HttpStatus.OK);
        } catch (JsonProcessingException ex){
            return new ResponseEntity<>("Error when trying to create Json", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void updatePrice(Price p, Long intersect_left, Long intersect_right, Long curr_left, Long curr_right){
        if (intersect_left.equals(-1)){
            p.setStart_date(new Date(intersect_right));
            priceService.add(p);
        } else {
            if (intersect_right.equals(Long.MAX_VALUE)){
                p.setEnd_date(new Date(intersect_left));
                priceService.add(p);
            } else {
                if (intersect_left.equals(curr_left)){
                    p.setStart_date(new Date(intersect_right));
                    priceService.add(p);
                } else
                if (intersect_right.equals(curr_right)){
                    p.setEnd_date(new Date(intersect_left));
                    priceService.add(p);

                }
                else {
                    p.setEnd_date(new Date(intersect_left));
                    priceService.add(p);
                    Price newPrice = new Price();
                    newPrice.setPrice(p.getPrice());
                    newPrice.setProductId(p.getProductId());
                    if (intersect_right!=-1)
                        newPrice.setStart_date(new Date(intersect_right));
                    if (curr_right != Long.MAX_VALUE)
                        newPrice.setEnd_date(new Date(curr_right));
                    priceService.add(newPrice);

                }
            }
        }
    }

    private void refreshPrices(Price price){
        List<Price> prices = priceService.getByProductId(price.getProductId());
        Long left_border = (price.getStart_date()== null) ? -1 : price.getStart_date().getTime();
        Long right_border = (price.getEnd_date() == null) ? Long.MAX_VALUE : price.getEnd_date().getTime();
        for (Price p : prices){
            Long curr_left = (p.getStart_date() == null) ? -1 : p.getStart_date().getTime();
            Long curr_right = (p.getEnd_date() == null) ? Long.MAX_VALUE : p.getEnd_date().getTime();

            Long intersect_left = Math.max(left_border, curr_left);
            Long intersect_right = Math.min(right_border, curr_right);

            //its inside new price
            if (curr_left.equals(intersect_left) && curr_right.equals(intersect_right)){
                priceService.deletePrice(p);
                priceService.add(price);
            } else
                //they intersect
                if (intersect_left < intersect_right){
                    updatePrice(p, intersect_left, intersect_right, curr_left, curr_right);
                }

        }
    }

}
