package com.ust.Security.service;

import com.ust.Security.model.Products;
import com.ust.Security.repository.Productrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Productservice {
    @Autowired
    private Productrepository repo;
    public Products addproduct(Products product) {
        return repo.save(product);
    }

    public List<Products> getallproducts() {
        return repo.findAll();
    }
}
