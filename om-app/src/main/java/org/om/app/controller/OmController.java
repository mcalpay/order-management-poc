package org.om.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.om.domain.dto.NewOrder;
import org.om.domain.dto.NewOrderResponse;
import org.om.domain.service.OrderService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/om")
@RequiredArgsConstructor
public class OmController {

    private final OrderService orderService;

    @Operation(summary = "hello world")
    @PostMapping("/create")
    public ResponseEntity<NewOrderResponse> createOrder(@RequestBody NewOrder order) {
        NewOrderResponse orderDto = orderService.create(order);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handIntegretiy() {
        return new ResponseEntity<>("Constraint conflicts.", HttpStatus.CONFLICT);
    }

}
