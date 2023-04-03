package com.GlobalMarket.ECommerce.Repository;

import com.GlobalMarket.ECommerce.Enum.ProductCategory;
import com.GlobalMarket.ECommerce.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product > findAllByProductCategory(ProductCategory productCategory);
}
