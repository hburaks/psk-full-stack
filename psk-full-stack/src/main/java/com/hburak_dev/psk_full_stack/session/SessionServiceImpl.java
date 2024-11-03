package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.Util;
import com.hburak_dev.psk_full_stack.common.PageResponse;
import com.hburak_dev.psk_full_stack.exception.SessionNotFoundException;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.user.UserRepository;
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

    private final UserRepository userRepository;

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
    public List<SessionResponse> getMySessions(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        List<Session> sessions = sessionRepository.findByUserId(user.getId());

        return sessions
                .stream()
                .map(sessionMapper::toSessionResponse)
                .toList();
    }

    @Override
    public Integer createUserSession(UserSessionRequest userSessionRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        return createSessionWithIdAndDate(user, userSessionRequest.getDate());
    }

    private Integer createSessionWithIdAndDate(User user, LocalDateTime date) {

        LocalDateTime fullHour = Util.toFullHour(date);

        boolean sessionExists = sessionRepository.existsByDate(fullHour);
        if (!sessionExists) {
            Session session = sessionMapper.toSession(fullHour, user);
            session.setCreatedBy(user.getId()); // TODO: is this necessary
            Session savedSession = sessionRepository.save(session);
            return savedSession.getId();
        } else {
            throw new SessionNotFoundException("Bu zaman diliminde takvim müsait değil: " + fullHour);
        }
    }

    @Override
    public Integer createSessionForUserV2(UserSessionRequest userSessionRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SessionNotFoundException("Bu id ile kullanıcı bulunamadı: " + userId));

        return createSessionWithIdAndDate(user, userSessionRequest.getDate());
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
                .map(sessionMapper::toSessionResponseV2)
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
    public SessionResponseV2 addNoteToSessionForUserV2(SessionUserNoteRequest sessionNoteRequest) {
        Optional<Session> sessionOpt = sessionRepository.findById(sessionNoteRequest.getSessionId());

        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setNoteForUser(sessionNoteRequest.getNoteForUser());

            Session savedSession = sessionRepository.save(session);

            return sessionMapper.toSessionResponseV2(savedSession);
        } else {
            throw new SessionNotFoundException("Bu id ile seans bulunamadı: " + sessionNoteRequest.getSessionId());
        }
    }

    @Override
    public SessionResponseV2 addNoteToSessionForPsychologistV2(SessionPsychologistNoteRequest sessionNoteRequest) {
        Optional<Session> sessionOpt = sessionRepository.findById(sessionNoteRequest.getSessionId());

        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setNoteForPsychologist(sessionNoteRequest.getNoteForPsychologist());

            Session savedSession = sessionRepository.save(session);
            // TODO: last modified by alanı test edilmeli.

            return sessionMapper.toSessionResponseV2(savedSession);
        } else {
            throw new SessionNotFoundException("Bu id ile seans bulunamadı: " + sessionNoteRequest.getSessionId());
        }
    }

    @Override
    public Integer updateSessionStatusV2(SessionStatusRequest sessionStatusRequest) {
        Optional<Session> sessionOpt = sessionRepository.findById(sessionStatusRequest.getSessionId());

        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setSessionStatus(sessionStatusRequest.getSessionStatusType());

            Session savedSession = sessionRepository.save(session);

            return savedSession.getId();
        } else {
            throw new SessionNotFoundException("Bu id ile seans bulunamadı: " + sessionStatusRequest.getSessionId());
        }
    }

    @Override
    public Integer updateSessionDateV2(SessionDateRequest sessionDateRequest) {
        Optional<Session> sessionOpt = sessionRepository.findById(sessionDateRequest.getSessionId());

        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setDate(sessionDateRequest.getDate());

            Session savedSession = sessionRepository.save(session);

            return savedSession.getId();
        } else {
            throw new SessionNotFoundException("Bu id ile seans bulunamadı: " + sessionDateRequest.getSessionId());
        }
    }

    @Override
    public PageResponse<UserWithSessionResponse> getAllUsersWithSessionV2(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<User> usersWithSessions = userRepository.findAllUsersWithSessions(pageable);


        List<UserWithSessionResponse> responseList = usersWithSessions
                .stream()
                .map(user -> UserWithSessionResponse.builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .sessionIds(sessionMapper.toSessionIdList(user.getSessions()))
                        .build())
                .toList();

        return new PageResponse<>(
                responseList,
                usersWithSessions.getNumber(),
                usersWithSessions.getSize(),
                usersWithSessions.getTotalElements(),
                usersWithSessions.getTotalPages(),
                usersWithSessions.isFirst(),
                usersWithSessions.isLast()
        );
    }

    @Override
    public List<PublicSessionResponse> makeUnavailableV2(List<LocalDateTime> unavailableTimes) {

        List<LocalDateTime> fullHoursUnavailable = Util.toFullHours(unavailableTimes);
        fullHoursUnavailable.forEach(time -> {
            boolean sessionExists = sessionRepository.existsByDate(time);

            if (!sessionExists) {
                Session session = Session.builder()
                        .date(time)
                        .sessionStatus(SessionStatusType.UNAVAILABLE)
                        .isSessionPaid(false)
                        .isMock(true)
                        .build();
                sessionRepository.save(session);
            }
        });

        return getAllSessionsWeekly(fullHoursUnavailable.get(0));
    }

    @Override
    public List<PublicSessionResponse> makeAvailableV2(List<LocalDateTime> availableTimes) {

        List<LocalDateTime> fullHoursAvailable = Util.toFullHours(availableTimes);

        fullHoursAvailable.forEach(time -> {
            Session sessionExists = sessionRepository.findByDate(time);
            if (sessionExists.isMock() || sessionExists.getSessionStatus() == SessionStatusType.CANCELED) {
                sessionRepository.delete(sessionExists);
            }
        });

        return getAllSessionsWeekly(fullHoursAvailable.get(0));
    }


    @Override
    public SessionResponseV2 updateSessionPaidStatusV2(Boolean isPaid, Integer sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Bu id ile seans bulunamadı: " + sessionId));

        session.setSessionPaid(isPaid);
        Session savedSession = sessionRepository.save(session);

        return sessionMapper.toSessionResponseV2(savedSession);
    }


    @Override
    public List<SessionResponseV2> getAllSessionsOfUserV2(Integer userId) {
        List<Session> sessions = sessionRepository.findByUserId(userId);

        return sessions.stream()
                .map(sessionMapper::toSessionResponseV2)
                .toList();
    }


}
