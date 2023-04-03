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

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;


    @Autowired
    JavaMailSender emailSender;


    public String addToCart(OrderRequestDto orderRequestDto) throws CustomerNotFoundException, ProductNotFoundException, InsufficientResourcesException {
        //check customer is available
        Customer customer;
        try {
            customer = customerRepository.findById(orderRequestDto.getCustomerId()).get();
        } catch (Exception e) {
            throw new CustomerNotFoundException("Invalid customer id");
        }

        //check product is available
        Product product;
        try {
            product = productRepository.findById(orderRequestDto.getProductId()).get();
        } catch (Exception e) {
            throw new ProductNotFoundException("Invalid product id");
        }

        //check the quantity of product
        if (product.getQuantity() < orderRequestDto.getRequiredQuantity()) {
            throw new InsufficientResourcesException("Insufficient quantity sorry...");
        }

        // we are not making new cart because each customer is having only one cart
        Cart cart = customer.getCart();

        int newCost = cart.getCartTotal() + (orderRequestDto.getRequiredQuantity() * product.getPrice());
        cart.setCartTotal(newCost);


        // ** firstly make the item and set the cart in it because child serve first by their parent
        // add item to current cart
        Item item = new Item();
        item.setRequiredQuantity(orderRequestDto.getRequiredQuantity());

        // we are adding cartId and productId in item table.
        item.setCart(cart);
        item.setProduct(product);

        // ** after child being served not parent can add the item in its database
        // set the item to cart because it is parent
        cart.getItems().add(item);

        customerRepository.save(customer);


        // return the string
        return "Item has been added to the cart";
    }


    public List<OrderResponseDto> checkoutCart(int customerId) throws CustomerNotFoundException {
        // check the customer
        Customer customer;
        try {
            customer = customerRepository.findById(customerId).get();
        } catch (Exception e) {
            throw new CustomerNotFoundException("Invalid customer Id");
        }

        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
        Cart cart = customer.getCart();
        int totalCost = cart.getCartTotal();
        int deliveryCharge = 0;
        if(cart.getCartTotal() < 499) {
            deliveryCharge = 50;
        }
        for (Item item : cart.getItems()) {
            Ordered ordered = new Ordered();
            ordered.setTotalCost(item.getRequiredQuantity() * item.getProduct().getPrice());
            ordered.setDeliveryCharge(deliveryCharge);
            ordered.setCustomer(customer);
            ordered.getOrderedItems().add(item);
            String cardNo = "";
            Card card = customer.getCards().get(0);
            for (int i = 0; i < card.getCardNo().length() - 4; i++) {
                cardNo += 'X';
            }
            cardNo += card.getCardNo().substring(card.getCardNo().length() - 4);
            ordered.setCardUsedForPayment(cardNo);

            int leftQuantity = item.getProduct().getQuantity() - item.getRequiredQuantity();
            if(leftQuantity <= 0)
            {
                item.getProduct().setProductStatus(ProductStatus.OUT_OF_STOCK);
            }
            item.getProduct().setQuantity(leftQuantity);

            customer.getOrders().add(ordered);
            item.setOrder(ordered);

            // prepare the response Dto
            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .productName(item.getProduct().getProductName())
                    .orderDate(item.getOrder().getOrderDate())
                    .quantityOrdered(item.getRequiredQuantity())
                    .cardUsedForPayment(cardNo)
                    .itemPrice(item.getProduct().getPrice())
                    .totalCost(ordered.getTotalCost())
                    .deliveryCharge(deliveryCharge)
                    .build();


            orderResponseDtos.add(orderResponseDto);
        }

        cart.setItems(new ArrayList<>());
        cart.setCartTotal(0);

        customerRepository.save(customer);

        String text = "Congrats !!. your order with total value "+ totalCost + " has been placed." ;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("chiragmittal36279@gmail.com");
        message.setTo(customer.getEmail());
        message.setSubject("Order placed via cart");
        message.setText(text);
        emailSender.send(message);


        return orderResponseDtos;
    }
}