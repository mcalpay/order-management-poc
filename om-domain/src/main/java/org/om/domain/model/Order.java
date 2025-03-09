package org.om.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "orders")
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
// Order: customerId, assetName, orderSide, size, price, status, createDate
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long customerId;

    private String assetName;

    private int size;

    private int price;

    private OrderStatus status;

    private OrderSide orderSide;

    private LocalDateTime createDate;

    public boolean isSelling() {
        return OrderSide.SELL.equals(orderSide);
    }
}
