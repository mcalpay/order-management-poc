package org.om.app.service;

import lombok.Getter;
import org.om.domain.model.OmUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class AppUser  extends User {

    private final Long id;

    public AppUser(OmUser user) {
        super(user.getUserName(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name())));
        this.id = user.getId();
    }

}
