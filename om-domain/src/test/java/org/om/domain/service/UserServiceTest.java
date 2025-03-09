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
import org.om.domain.model.OmUser;
import org.om.domain.model.Order;
import org.om.domain.model.OrderStatus;
import org.om.domain.repository.AssetRepository;
import org.om.domain.repository.OrderRepository;
import org.om.domain.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private OmUser user;

    @BeforeEach
    void setUp() {
        user = OmUser.builder().userName("admin").build();
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUserName("admin")).thenReturn(Optional.of(user));
        Optional<OmUser> user = userService.findByUsername("admin");
        assertTrue(user.isPresent());
        assertEquals("admin", user.get().getUserName());
    }
}
