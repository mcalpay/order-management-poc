package org.om.domain.repository;

import org.om.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdAndCreateDateBetweenOrderByCreateDateDesc(Long customerId,
                                                                          LocalDateTime startDate, LocalDateTime endDate);

}
