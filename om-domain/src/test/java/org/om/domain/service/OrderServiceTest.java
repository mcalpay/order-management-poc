package org.om.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.om.domain.dto.AssetResponse;
import org.om.domain.dto.NewOrder;
import org.om.domain.dto.NewOrderResponse;
import org.om.domain.dto.OrderResponse;
import org.om.domain.exception.OmAuthorizationException;
import org.om.domain.exception.OmException;
import org.om.domain.model.Asset;
import org.om.domain.model.Order;
import org.om.domain.model.OrderStatus;
import org.om.domain.repository.AssetRepository;
import org.om.domain.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private OrderService orderService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private NewOrder newOrder;
    private Asset testAsset;

    @BeforeEach
    void setUp() {
        testAsset = Asset.builder().customerId(1L).assetName("BTC").size(10).usableSize(10).build();
        newOrder = new NewOrder(1L, "BTC", "SELL", 10, 10);
    }

    @Test
    void testCreate_WhenSellingAndHasEnoughAsset() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(testAsset));
        when(orderRepository.save(any(Order.class))).thenReturn(newOrder.toEntity());
        NewOrderResponse response = orderService.create(newOrder);
        assertNotNull(response);
        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreate_WhenSellingAndNotEnoughAsset() {
        testAsset.setUsableSize(3);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(testAsset));
        assertThrows(OmException.class, () -> orderService.create(newOrder));
    }

    @Test
    void testCreate_WhenBuyingAndHasEnoughFunds() {
        Asset tryAsset = Asset.builder().customerId(1L).assetName("TRY").size(2000).usableSize(2000).build();
        NewOrder buyOrder = new NewOrder(1L, "BTC", "BUY", 200, 1);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(Optional.of(tryAsset));
        when(orderRepository.save(any(Order.class))).thenReturn(buyOrder.toEntity());
        NewOrderResponse response = orderService.create(buyOrder);
        assertNotNull(response);
        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testListOrders() {
        LocalDateTime start = LocalDate.parse("2024-01-01", dateFormatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse("2024-02-01", dateFormatter).atStartOfDay();
        when(orderRepository.findByCustomerIdAndCreateDateBetweenOrderByCreateDateDesc(1L, start, end))
                .thenReturn(List.of(newOrder.toEntity()));
        List<OrderResponse> responses = orderService.listOrders(1L, "2024-01-01", "2024-02-01");
        assertEquals(1, responses.size());
    }

    @Test
    void testDeleteOrder_WhenAuthorizedAndPending() {
        Order pendingOrder = Order.builder().customerId(1L).assetName("Product A").price(2).size(100).build();
        pendingOrder.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(pendingOrder));
        orderService.deleteOrder(1L, 1L, false);
        verify(orderRepository, times(1)).save(pendingOrder);
        assertEquals(OrderStatus.CANCELED, pendingOrder.getStatus());
    }

    @Test
    void testDeleteOrder_WhenNotAuthorized() {
        Order order = Order.builder().customerId(1L).assetName("Product A").price(2).size(100).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(OmAuthorizationException.class, () -> orderService.deleteOrder(1L, 2L, false));
    }

    @Test
    void testDeleteOrder_WhenNoPendingOrder() {
        Order matchedOrder = Order.builder().customerId(1L).assetName("Product A").price(2).size(100).build();
        matchedOrder.setStatus(OrderStatus.MATCHED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(matchedOrder));
        assertThrows(OmException.class, () -> orderService.deleteOrder(1L, 1L, true));
    }

    @Test
    void testListAssets() {
        when(assetRepository.findByCustomerId(1L)).thenReturn(List.of(testAsset));
        List<AssetResponse> responses = orderService.listAssets(1L);
        assertEquals(1, responses.size());
    }
}
