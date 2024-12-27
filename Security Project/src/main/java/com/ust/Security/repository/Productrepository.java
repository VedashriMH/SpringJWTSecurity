package com.ust.Security.repository;

import com.ust.Security.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Productrepository extends JpaRepository<Products, Integer> {

}
