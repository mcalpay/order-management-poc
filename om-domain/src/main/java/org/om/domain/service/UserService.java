package org.om.domain.service;

import lombok.RequiredArgsConstructor;
import org.om.domain.model.OmUser;
import org.om.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Optional<OmUser> findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }
}
