package com.GlobalMarket.ECommerce.Service;

import com.GlobalMarket.ECommerce.Model.Cart;
import com.GlobalMarket.ECommerce.Model.Customer;
import com.GlobalMarket.ECommerce.Repository.CustomerRepository;
import com.GlobalMarket.ECommerce.RequestDTO.CustomerRequestDto;
import com.GlobalMarket.ECommerce.convertor.CustomerConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public String addCustomer(CustomerRequestDto customerRequestDto){

        Customer customer = CustomerConvertor.CustomerRequestDtoToCustomer(customerRequestDto);

        // allocate a cart to customer
        Cart cart = new Cart();
        cart.setCartTotal(0);
        cart.setCustomer(customer);

        // set cart in customer
        customer.setCart(cart);

        customerRepository.save(customer);
        return "Congrats !! Welcome to Global Market.";
    }
}
