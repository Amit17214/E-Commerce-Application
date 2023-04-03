package com.GlobalMarket.ECommerce.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="ordered")
public class Ordered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @CreationTimestamp
    private Date orderDate;

    private int totalCost;

    private int deliveryCharge;

    private String cardUsedForPayment;

    @ManyToOne
    @JoinColumn
    Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    List<Item> orderedItems = new ArrayList<>();

}
