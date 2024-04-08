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
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "type")
    private String type;

    @Column(name = "create_date")
    private Date createDate;
    
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    List<Product> products;

    // Constructors, getters, and setters
    // You can generate constructors and getters/setters using your IDE or manually
}