package org.om.domain.dto;

import org.om.domain.model.Order;

import java.time.LocalDateTime;

public record NewOrderResponse(Long id, LocalDateTime createDate, String status) {

    public static NewOrderResponse fromOrder(Order save) {
        return new NewOrderResponse(save.getId(), save.getCreateDate(), save.getStatus().name());
    }

}
