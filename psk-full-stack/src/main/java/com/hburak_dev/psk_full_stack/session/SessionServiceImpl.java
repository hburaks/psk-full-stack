package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    public PageResponse<SessionResponse> getMySessions(Authentication connectedUser) {
        return null;
    }

    @Override
    public Integer createUserSession(UserSessionRequest userSessionRequest, Authentication connectedUser) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> cancelUserSession(Integer id, Authentication connectedUser) {
        return null;
    }

    @Override
    public PageResponse<SessionResponseV2> getAllSessionsV2(Authentication connectedUser) {
        return null;
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
}
