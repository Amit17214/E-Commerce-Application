package com.GlobalMarket.ECommerce.Model;

import com.GlobalMarket.ECommerce.Enum.ProductCategory;
import com.GlobalMarket.ECommerce.Enum.ProductStatus;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
     private String productName;
     private int price;
     private int quantity;
     @Enumerated(EnumType.STRING)
     ProductCategory productCategory;

     @Enumerated(EnumType.STRING)
     ProductStatus productStatus;

     @ManyToOne
     @JoinColumn
     Seller seller;

     @OneToOne(mappedBy = "product",cascade = CascadeType.ALL)
     Item item;


}
