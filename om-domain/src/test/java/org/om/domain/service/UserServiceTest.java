package org.om.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.om.domain.model.OmUser;
import org.om.domain.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
