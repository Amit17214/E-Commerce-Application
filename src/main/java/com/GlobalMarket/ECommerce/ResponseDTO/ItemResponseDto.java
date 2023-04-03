package com.GlobalMarket.ECommerce.ResponseDTO;

import com.GlobalMarket.ECommerce.Enum.ProductCategory;
import com.GlobalMarket.ECommerce.Enum.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponseDto {

    private String productName;
    private int price;
    private ProductCategory productCategory;
    private ProductStatus productStatus;
}
