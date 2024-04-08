package com.toko.entity;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Productdetails")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private int productDetailId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "size")
    private String size;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "productdetails")
//    List<Product> products;
    // Constructors, getters, and setters
    // You can generate constructors and getters/setters using your IDE or manually
}