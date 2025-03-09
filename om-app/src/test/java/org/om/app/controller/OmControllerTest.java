package org.om.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.om.domain.dto.NewOrder;
import org.om.domain.dto.NewOrderResponse;
import org.om.domain.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OmControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OmController omController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(omController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrder() throws Exception {
        NewOrder newOrder =  new NewOrder(1L, "BTC", "SELL", 10, 10);
        NewOrderResponse response = new NewOrderResponse(1L, LocalDateTime.now(), "PENDING");
        when(orderService.create(any(NewOrder.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/om/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()));
    }

    @Test
    void testListOrders() throws Exception {
        when(orderService.listOrders(anyLong(), anyString(), anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/om/list/1?startDate=2025-03-01&endDate=2025-04-01"))
                .andExpect(status().isOk());
    }

    @Test
    void testListAssets() throws Exception {
        when(orderService.listAssets(anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/om/asset/list/1"))
                .andExpect(status().isOk());
    }

}
