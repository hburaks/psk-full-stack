package com.hburak_dev.psk_full_stack.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN u.sessions s WHERE s IS NOT NULL")
    Page<User> findAllUsersWithSessions(Pageable pageable);
}
