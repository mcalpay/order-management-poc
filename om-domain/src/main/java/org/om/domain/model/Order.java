package org.om.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
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
    private Long id;

    private Serializable customerId;

    private String assetName;

    private int size;

    private int price;

    private OrderStatus status;

    private OrderSide orderSide;

    private LocalDateTime createDate;

}
