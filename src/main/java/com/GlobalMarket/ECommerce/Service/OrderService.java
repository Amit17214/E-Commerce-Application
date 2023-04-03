package com.GlobalMarket.ECommerce.Service;

import com.GlobalMarket.ECommerce.Enum.ProductStatus;
import com.GlobalMarket.ECommerce.Exception.CustomerNotFoundException;
import com.GlobalMarket.ECommerce.Exception.ProductNotFoundException;
import com.GlobalMarket.ECommerce.Model.*;
import com.GlobalMarket.ECommerce.Repository.CustomerRepository;
import com.GlobalMarket.ECommerce.Repository.ProductRepository;
import com.GlobalMarket.ECommerce.RequestDTO.OrderRequestDto;
import com.GlobalMarket.ECommerce.ResponseDTO.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;

    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) throws Exception {
        Customer customer;
        try{
            customer = customerRepository.findById(orderRequestDto.getCustomerId()).get();
        }
        catch(Exception e){
            throw new CustomerNotFoundException("Invalid Customer id !!");
        }

        Product product;
        try{
            product = productRepository.findById(orderRequestDto.getProductId()).get();
        }
        catch(Exception e){
            throw new ProductNotFoundException("Invalid Product Id");
        }

        if(product.getQuantity()<orderRequestDto.getRequiredQuantity()){
            throw new Exception("Sorry! Required quantity not available" );
        }

        // make the order
        Ordered ordered = new Ordered();
        int totalCost = orderRequestDto.getRequiredQuantity() * product.getPrice();
        int deliveryCharge = 0;
        if(totalCost < 500)
        {
            deliveryCharge = 50;
            totalCost += deliveryCharge;
        }
        ordered.setTotalCost(totalCost);
        ordered.setDeliveryCharge(deliveryCharge);


        // fetch the card details from customer so that I can add it in the ordered class
        Card card = customer.getCards().get(0);

        String cardNo = "";
        // it will set the cardNo with 'X'
        for(int i=0; i<card.getCardNo().length()-4; i++)
        {
            cardNo += 'X';
        }
        // it will set the last 4 digit of cardNo.
        cardNo += card.getCardNo().substring(card.getCardNo().length()-4);


        // set the card used for payment in ordered class
        ordered.setCardUsedForPayment(cardNo);

//        why? left quantity is not being updated their we are set the product in item and after that we are trying to update it.


        // make the new item
        Item item = new Item();
        item.setRequiredQuantity(orderRequestDto.getRequiredQuantity());
        // the below two lines will affect in the table it fills the orderedId and productId
        item.setProduct(product);
        item.setOrder(ordered);


        // set the item and customer in ordered class
        ordered.getOrderedItems().add(item);
        ordered.setCustomer(customer);


        int leftQuantity = product.getQuantity() - orderRequestDto.getRequiredQuantity();
        if(leftQuantity <= 0)
        {
            product.setProductStatus(ProductStatus.OUT_OF_STOCK);
        }
        product.setQuantity(leftQuantity);


        // set the order in the customer class
        customer.getOrders().add(ordered);
        Customer savedCustomer = customerRepository.save(customer);


        Ordered savedOrder = savedCustomer.getOrders().get(savedCustomer.getOrders().size()-1);


        // make the responseDto
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .productName(product.getProductName())
                .orderDate(savedOrder.getOrderDate())
                .quantityOrdered(orderRequestDto.getRequiredQuantity())
                .cardUsedForPayment(cardNo)
                .itemPrice(product.getPrice())
                .totalCost(ordered.getTotalCost())
                .deliveryCharge(deliveryCharge)
                .build();


        // email
        // these are the steps to send the email.
        String text = "Congrats !!. your order with total value "+ ordered.getTotalCost() + " has been placed." ;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("");
        message.setTo(customer.getEmail());
        message.setSubject("Order placed directly");
        message.setText(text);
        emailSender.send(message);

        return orderResponseDto;

    }
}
