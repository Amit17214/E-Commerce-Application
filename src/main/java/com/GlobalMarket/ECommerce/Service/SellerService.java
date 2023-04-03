package com.GlobalMarket.ECommerce.Service;

import com.GlobalMarket.ECommerce.Model.Seller;
import com.GlobalMarket.ECommerce.Repository.SellerRepository;
import com.GlobalMarket.ECommerce.RequestDTO.SellerRequestDto;
import com.GlobalMarket.ECommerce.convertor.SellerConvertor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepo;

    public String addSeller(SellerRequestDto sellerRequestDto){

        Seller seller = SellerConvertor.SellerRequestDtoToSeller(sellerRequestDto);
        sellerRepo.save(seller);

        return "Congrats! Tell me what do you want";

    }
}
