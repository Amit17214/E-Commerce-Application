package com.GlobalMarket.ECommerce.Service;

import com.GlobalMarket.ECommerce.Enum.ProductCategory;
import com.GlobalMarket.ECommerce.Exception.SellerNotFoundException;
import com.GlobalMarket.ECommerce.Model.Product;
import com.GlobalMarket.ECommerce.Model.Seller;
import com.GlobalMarket.ECommerce.Repository.ProductRepository;
import com.GlobalMarket.ECommerce.Repository.SellerRepository;
import com.GlobalMarket.ECommerce.RequestDTO.ProductRequestDto;
import com.GlobalMarket.ECommerce.ResponseDTO.ProductResponseDto;
import com.GlobalMarket.ECommerce.convertor.ProductConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SellerRepository sellerRepository;

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws SellerNotFoundException {

        Seller seller;

        try{
            seller = sellerRepository.findById(productRequestDto.getSellerId()).get();
        }
        catch(Exception e){
            throw new SellerNotFoundException("Invalid Seller Id");
        }

        Product product = ProductConvertor.productRequestDtotoProduct(productRequestDto);
        product.setSeller(seller);

        seller.getProducts().add(product);

        // save the seller and product - parent and child
        sellerRepository.save(seller);

        //prepare response
        ProductResponseDto productResponseDto = ProductConvertor.productToProductResponseDto(product);
        return productResponseDto;
    }

    public List<ProductResponseDto> getProductsByCategory(ProductCategory productCategory){

        List<Product> products= productRepository.findAllByProductCategory(productCategory);

        // prepare a list of response dtos
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product: products){
            ProductResponseDto productResponseDto = ProductConvertor.productToProductResponseDto(product);
            productResponseDtos.add(productResponseDto);
        }
        return productResponseDtos;
    }
}
