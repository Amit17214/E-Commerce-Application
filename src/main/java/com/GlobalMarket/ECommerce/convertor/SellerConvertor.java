package com.GlobalMarket.ECommerce.convertor;

import com.GlobalMarket.ECommerce.Model.Seller;
import com.GlobalMarket.ECommerce.RequestDTO.SellerRequestDto;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;

public class SellerConvertor {

    public static Seller SellerRequestDtoToSeller(SellerRequestDto sellerRequestDto) {


        return Seller.builder()
                .name(sellerRequestDto.getName())
                .email(sellerRequestDto.getEmail())
                .mobNo(sellerRequestDto.getMobNo())
                .panNo(sellerRequestDto.getPanNo())
                .products(new ArrayList<>())
                .build();
    }
}

