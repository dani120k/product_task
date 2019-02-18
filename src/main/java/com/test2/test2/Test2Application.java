package com.test2.test2;

import com.google.gson.Gson;
import com.test2.test2.model.Product;
import com.test2.test2.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class Test2Application {


	public static void main(String[] args) {
		SpringApplication.run(Test2Application.class, args);
	}

}

