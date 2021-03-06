package com.test2.test2.handlers;

import com.google.gson.Gson;
import com.test2.test2.model.Price;
import com.test2.test2.model.Product;
import com.test2.test2.service.PriceServiceImpl;
import com.test2.test2.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PriceHandler {
    @Autowired
    private PriceServiceImpl priceService;

    @Autowired
    private ProductServiceImpl productService;

    public String addNew(Price price, String productName){
        Product product = productService.findByName(productName);
        if (product == null)
            return "Product with this name doesnt exist";

        price.setProduct_id(product.getId());
        refreshPrices(price);
        Price addedPrice = priceService.add(price);
        if (addedPrice !=null) {
            return new Gson().toJson(addedPrice);
        }
        else
            return "Some error when create this price";
    }

    public String getAllPrices(){
        return new Gson().toJson(priceService.getAll());
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
                    newPrice.setProduct_id(p.getProduct_id());
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
        List<Price> prices = priceService.getByProductId(price.getProduct_id());
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
