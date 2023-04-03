package com.GlobalMarket.ECommerce.RequestDTO;

import com.GlobalMarket.ECommerce.Enum.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {

    private int sellerId;
    private String productName;
    private int price;
    private int quantity;

    private ProductCategory productCategory;
}
