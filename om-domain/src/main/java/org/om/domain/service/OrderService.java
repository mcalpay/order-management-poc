package org.om.domain.service;

import lombok.RequiredArgsConstructor;
import org.om.domain.dto.NewOrder;
import org.om.domain.dto.NewOrderResponse;
import org.om.domain.model.Order;
import org.om.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public NewOrderResponse create(NewOrder newOrder) {
        Order save = orderRepository.save(newOrder.toEntity());
        return NewOrderResponse.fromOrder(save);
    }

}
