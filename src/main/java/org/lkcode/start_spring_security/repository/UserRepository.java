package org.lkcode.start_spring_security.repository;

import org.lkcode.start_spring_security.model.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);
}
