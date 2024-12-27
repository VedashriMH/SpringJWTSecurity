package com.ust.Security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id
    public int id;
    public String name;
    public String price;
    public int quantity;
    public String expDate;
    public String packDate;

}
