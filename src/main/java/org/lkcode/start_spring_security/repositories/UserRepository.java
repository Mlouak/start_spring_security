package org.lkcode.start_spring_security.repositories;

import org.lkcode.start_spring_security.models.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);
}
