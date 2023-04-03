package com.GlobalMarket.ECommerce.Controller;

import com.GlobalMarket.ECommerce.RequestDTO.CustomerRequestDto;
import com.GlobalMarket.ECommerce.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/add")
    public String addCustomer(@RequestBody CustomerRequestDto customerRequestDto){

        return customerService.addCustomer((customerRequestDto));

    }
}
