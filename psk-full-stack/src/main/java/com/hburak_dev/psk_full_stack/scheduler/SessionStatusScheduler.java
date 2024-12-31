package com.hburak_dev.psk_full_stack.scheduler;

import com.hburak_dev.psk_full_stack.session.Session;
import com.hburak_dev.psk_full_stack.session.SessionRepository;
import com.hburak_dev.psk_full_stack.session.SessionStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionStatusScheduler {

  private final SessionRepository sessionRepository;

  @Scheduled(cron = "0 50 * * * *")
  @Transactional
  public void updateCompletedSessions() {
    LocalDateTime now = LocalDateTime.now();

    List<Session> scheduledSessions = sessionRepository.findBySessionStatusAndDateBefore(
        SessionStatusType.APPOINTMENT_SCHEDULED,
        now);

    for (Session session : scheduledSessions) {
      session.setSessionStatus(SessionStatusType.COMPLETED);
      sessionRepository.save(session);
      log.info("Session {} marked as completed", session.getId());
    }
  }
}