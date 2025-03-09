package org.om.domain.dto;

import org.om.domain.model.Order;

import java.time.LocalDateTime;

public record OrderResponse(Long id, LocalDateTime createDate, String status, String assetName, int size, int price) {
    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(order.getId(), order.getCreateDate(), order.getStatus().name(), order.getAssetName(),
                order.getSize(), order.getPrice());
    }
}
