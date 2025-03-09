package org.om.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.om.domain.model.OmUser;
import org.om.domain.model.Role;
import org.om.domain.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private OmUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new OmUser();
        testUser.setUserName("testUser");
        testUser.setPassword("password123");
        testUser.setRole(Role.ROLE_CUSTOMER);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userService.findByUsername("unknownUser")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userDetailsService.loadUserByUsername("unknownUser"));
    }
}
