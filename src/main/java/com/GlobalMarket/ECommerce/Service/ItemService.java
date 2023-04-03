package com.GlobalMarket.ECommerce.Service;

import com.GlobalMarket.ECommerce.Exception.ProductNotFoundException;
import com.GlobalMarket.ECommerce.Model.Item;
import com.GlobalMarket.ECommerce.Model.Product;
import com.GlobalMarket.ECommerce.Repository.ProductRepository;
import com.GlobalMarket.ECommerce.ResponseDTO.ItemResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    ProductRepository productRepository;
    public ItemResponseDto viewItem(int productId) throws ProductNotFoundException {

        Product product;
        try{
            product = productRepository.findById(productId).get();
        }
        catch(Exception e){
            throw new ProductNotFoundException("Sorry! Invalid product id.");
        }

        Item item = Item.builder()
                .requiredQuantity(0)
                .product(product)
                .build();

        // map item to product
        product.setItem(item);

        // save both item and product
        productRepository.save(product);

        // prepare the response dto
        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .productCategory(product.getProductCategory())
                .productStatus(product.getProductStatus())
                .build();

        return itemResponseDto;

    }
}
