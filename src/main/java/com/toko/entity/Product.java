package com.toko.entity;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.web.bind.annotation.Mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "img")
    private String img;

    @Column(name = "desc_materrial")
    private String descMaterial;

    @Column(name = "desc_status")
    private String descStatus;

    @Column(name = "desc_pattern")
    private String descPattern;

    @Column(name = "discount")
    private int discount;

    @Column(name = "create_date")
    private Date createDate;
    
    @Column(name = "pro_status")
    private String status;
    
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    List<ProductDetail> details;
    // Constructors, getters, and setters
    // You can generate constructors and getters/setters using your IDE or manually
}