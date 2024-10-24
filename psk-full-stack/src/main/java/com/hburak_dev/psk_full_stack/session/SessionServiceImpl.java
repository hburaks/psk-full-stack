package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import com.hburak_dev.psk_full_stack.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final SessionMapper sessionMapper;

    @Override
    public List<PublicSessionResponse> getAllSessionsWeekly(LocalDateTime dateTime) {

        LocalDateTime startOfWeek = dateTime.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime endOfWeek = dateTime.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);

        List<Session> sessions = sessionRepository.findByDateBetween(startOfWeek, endOfWeek);

        return sessions.stream()
                .filter(session -> session.getSessionStatus() != SessionStatusType.CANCELED)
                .map(sessionMapper::toPublicSessionResponse).toList();
    }

    @Override
    public PageResponse<SessionResponse> getMySessions(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Session> sessions = sessionRepository.findByUserId(pageable, user.getId());

        List<SessionResponse> sessionResponseList = sessions
                .stream()
                .map(sessionMapper::toSessionResponse)
                .toList();

        return new PageResponse<>(
                sessionResponseList,
                sessions.getNumber(),
                sessions.getSize(),
                sessions.getTotalElements(),
                sessions.getTotalPages(),
                sessions.isFirst(),
                sessions.isLast()
        );
    }

    @Override
    public Integer createUserSession(UserSessionRequest userSessionRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Session session = sessionMapper.toSession(userSessionRequest, user);
        session.setCreatedBy(user.getId());
        Session savedSession = sessionRepository.save(session);

        return savedSession.getId();
    }

    @Override
    public ResponseEntity<Boolean> cancelUserSession(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Optional<Session> sessionOpt = sessionRepository.findById(id);
        return sessionOpt.map(session -> {
            if (session.getUser().getId().equals(user.getId()) && !session.isSessionPaid()) {
                session.setSessionStatus(SessionStatusType.CANCELED);

                sessionRepository.save(session);

                return ResponseEntity.ok(true);
            } else if (session.getUser().getId().equals(user.getId()) && session.isSessionPaid()) {

                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(false);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
            }
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }

    @Override
    public PageResponse<SessionResponseV2> getAllSessionsV2(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Session> sessions = sessionRepository.findAll(pageable);

        List<SessionResponseV2> sessionResponseList = sessions
                .stream()
                .map(sessionMapper::toSessionResponseV2) // Oturumları dönüşüm yapın
                .toList();

        return new PageResponse<>(
                sessionResponseList,
                sessions.getNumber(),
                sessions.getSize(),
                sessions.getTotalElements(),
                sessions.getTotalPages(),
                sessions.isFirst(),
                sessions.isLast()
        );
    }

    @Override
    public SessionResponseV2 addNoteToSessionForUserV2(SessionUserNoteRequest sessionNoteRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public SessionResponseV2 addNoteToSessionForPsychologistV2(SessionPsychologistNoteRequest sessionNoteRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public Integer updateSessionStatusV2(SessionStatusRequest sessionStatusRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public Integer updateSessionDateV2(SessionDateRequest sessionStatusRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public PageResponse<UserWithSessionResponse> getAllUsersWithSessionV2(int page, int size, Authentication connectedUser) {
        return null;
    }

    @Override
    public SessionResponseV2 makeUnavailableV2(SessionAvailabilityRequest sessionAvailabilityRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public SessionResponseV2 makeAvailableV2(SessionAvailabilityRequest sessionAvailabilityRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public SessionResponseV2 makeSessionPaid(Boolean isPaid, Authentication connectedUser) {
        return null;
    }

    @Override
    public SessionResponseV2 makeSessionNotPaid(Boolean isPaid, Authentication connectedUser) {
        return null;
    }
}
