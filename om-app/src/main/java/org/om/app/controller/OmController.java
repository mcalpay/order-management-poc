package org.om.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.om.app.service.AppUser;
import org.om.domain.dto.AssetResponse;
import org.om.domain.dto.NewOrder;
import org.om.domain.dto.NewOrderResponse;
import org.om.domain.dto.OrderResponse;
import org.om.domain.exception.OmAuthorizationException;
import org.om.domain.exception.OmException;
import org.om.domain.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/om")
@Tag(name = "Order Management", description = "APIs for managing orders")
@RequiredArgsConstructor
public class OmController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and #order.customerId == principal.id)")
    @PostMapping("/create")
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order for the specified customer with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or missing fields"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<NewOrderResponse> createOrder(
            @Parameter(description = "Details of the new order to be created", required = true)
            @RequestBody NewOrder order) {
        NewOrderResponse orderDto = orderService.create(order);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and #customerId == principal.id)")
    @GetMapping("/list/{customerId}")
    @Operation(
            summary = "List orders",
            description = "Retrieves a list of orders for a given customer within a specified date range."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<OrderResponse> listOrders(
            @Parameter(description = "Customer id to filter orders", example = "123", required = true)
            @PathVariable Long customerId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2025-03-01", required = true)
            @RequestParam String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2025-04-01", required = true)
            @RequestParam String endDate) {
        return orderService.listOrders(customerId, startDate, endDate);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and #customerId == principal.id)")
    @GetMapping("/asset/list/{customerId}")
    @Operation(
            summary = "List assets for given customerId",
            description = "Retrieves a list of assets for a given customer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<AssetResponse> listAssets(@Parameter(description = "Customer id to filter assets", example = "123", required = true)
                                          @PathVariable Long customerId) {
        return orderService.listAssets(customerId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/{orderId}")
    @Operation(
            summary = "Delete an order",
            description = "Deletes an order by its ID. Returns a success message if deleted."
    )
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId, @AuthenticationPrincipal AppUser principal) {
        orderService.deleteOrder(orderId, principal.getId(), principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));
        return ResponseEntity.ok("Order with ID " + orderId + " deleted successfully.");
    }

    @ExceptionHandler(OmException.class)
    public ResponseEntity<String> omExceptionHandler(OmException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OmAuthorizationException.class)
    public ResponseEntity<String> omExceptionHandler(OmAuthorizationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}
