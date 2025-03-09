package org.om.domain.repository;

import org.om.domain.model.OmUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<OmUser, Long> {

    Optional<OmUser> findByUserName(String userName);

}
