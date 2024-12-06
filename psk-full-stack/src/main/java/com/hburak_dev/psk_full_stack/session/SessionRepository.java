package com.hburak_dev.psk_full_stack.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findByDateBetween(LocalDateTime startOfWeek, LocalDateTime endOfWeek);

    List<Session> findByUserId(Integer userId);

    boolean existsByDate(LocalDateTime date);

    Session findByDate(LocalDateTime date);

    /**
     * Finds the first session after the given date that does not have the specified
     * session status,
     * ordered by date in ascending order.
     * 
     * @param date          The date to search after
     * @param sessionStatus The session status to exclude
     * @return The first matching Session, or null if none found
     */
    Session findFirstByDateAfterAndSessionStatusNotOrderByDateAsc(LocalDateTime date, SessionStatusType sessionStatus);


}
