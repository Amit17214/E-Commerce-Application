package com.GlobalMarket.ECommerce.convertor;

import com.GlobalMarket.ECommerce.Enum.ProductStatus;
import com.GlobalMarket.ECommerce.Model.Product;
import com.GlobalMarket.ECommerce.RequestDTO.ProductRequestDto;
import com.GlobalMarket.ECommerce.ResponseDTO.ProductResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductConvertor {

    public static Product productRequestDtotoProduct(ProductRequestDto productRequestDto){

        return Product.builder()
                .productName(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .quantity(productRequestDto.getQuantity())
                .productCategory(productRequestDto.getProductCategory())
                .productStatus(ProductStatus.AVAILABLE)
                .build();
    }

    public static ProductResponseDto productToProductResponseDto(Product product){

        return ProductResponseDto.builder()
                .name(product.getProductName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .productStatus(product.getProductStatus())
                .build();
    }
}
