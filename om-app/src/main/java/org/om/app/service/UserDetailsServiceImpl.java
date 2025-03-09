package org.om.app.service;

import lombok.RequiredArgsConstructor;
import org.om.domain.model.OmUser;
import org.om.domain.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<OmUser> byUsernameOpt = userService.findByUsername(username);
        OmUser omUser = byUsernameOpt.orElseThrow();
        return new AppUser(omUser);
    }

}
