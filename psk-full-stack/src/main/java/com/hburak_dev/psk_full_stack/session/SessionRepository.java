package com.hburak_dev.psk_full_stack.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findByDateBetween(LocalDateTime startOfWeek, LocalDateTime endOfWeek);

}
