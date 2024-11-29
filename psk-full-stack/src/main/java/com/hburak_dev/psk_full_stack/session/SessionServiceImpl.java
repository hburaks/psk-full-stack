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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final SessionMapper sessionMapper;

    private List<PublicSessionResponse> getAllSessionsWeekly(LocalDateTime dateTime) {

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
    public Integer createUserSession(LocalDateTime localDateTime, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        return createSessionWithIdAndDate(user, localDateTime);
    }

    private Integer createSessionWithIdAndDate(User user, LocalDateTime date) {
        LocalDateTime fullHour = Util.toFullHour(date);
        validateSessionDate(fullHour);

        if (sessionRepository.existsByDate(fullHour)) {
            throw new SessionNotFoundException("Bu zaman diliminde takvim müsait değil: " + fullHour);
        }

        Session session = sessionMapper.toSession(fullHour, user);
        session.setCreatedBy(user.getId()); // TODO: is this necessary
        Session savedSession = sessionRepository.save(session);
        return savedSession.getId();
    }

    private void validateSessionDate(LocalDateTime fullHour) {
        if (fullHour.isBefore(LocalDateTime.now())) {
            throw new SessionNotFoundException("Geçmiş tarihli seans oluşturulamaz: " + fullHour);
        }
        if (fullHour.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new SessionNotFoundException("Pazar günü seans oluşturulamaz: " + fullHour);
        }
        if (fullHour.getHour() >= 20 || fullHour.getHour() < 10) {
            throw new SessionNotFoundException(
                    "Sabah 10'dan önce veya akşam 20'den sonra seans oluşturulamaz: " + fullHour);
        }
        if (fullHour.isAfter(LocalDateTime.now().plusDays(60))) {
            throw new SessionNotFoundException("60 günden daha ileri bir tarih için seans oluşturulamaz: " + fullHour);
        }
    }

    @Override
    public Integer createSessionForUserV2(LocalDateTime date, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SessionNotFoundException("Bu id ile kullanıcı bulunamadı: " + userId));

        return createSessionWithIdAndDate(user, date);
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
                sessions.isLast());
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
                usersWithSessions.isLast());
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

    @Override
    public List<DailyCalendarResponse> getWeeklyCalendar(LocalDateTime weekDate) {

        List<Session> sessions = getAllFilteredSessionsWeekly(weekDate);

        Map<DayOfWeek, List<Session>> sessionsByDayOfWeek = sessions.stream()
                .collect(Collectors.groupingBy(session -> session.getDate().getDayOfWeek()));

        List<DailyCalendarResponse> dailyCalendarResponses = new java.util.ArrayList<>();

        for (DayOfWeek day : Arrays.stream(DayOfWeek.values())
                .filter(d -> d != DayOfWeek.SUNDAY)
                .toList()) {

            List<HourlySessionResponse> dayResponses = new java.util.ArrayList<>();
            List<Session> existingSessions = sessionsByDayOfWeek.getOrDefault(day,
                    new java.util.ArrayList<>());

            LocalDateTime currentDate = weekDate.with(day);
            for (int hour = 10; hour <= 19; hour++) {
                LocalDateTime timeSlot = currentDate.withHour(hour).withMinute(0).withSecond(0).withNano(0);

                boolean isAvailable = existingSessions.stream()
                        .noneMatch(session -> session.getDate().equals(timeSlot));

                if (day == DayOfWeek.SATURDAY && timeSlot.getHour() >= 18) {
                    isAvailable = false;
                }

                dayResponses.add(HourlySessionResponse.builder()
                        .date(timeSlot)
                        .isAvailable(isAvailable)
                        .build());
            }

            dailyCalendarResponses.add(DailyCalendarResponse.builder()
                    .dayOfWeek(day)
                    .sessions(dayResponses)
                    .build());
        }

        return dailyCalendarResponses;
    }

    private List<Session> getAllFilteredSessionsWeekly(LocalDateTime weekDate) {
        LocalDateTime now = LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0)
                .withNano(0);
        LocalDateTime fourWeeksFromNow = now.plusWeeks(5);

        if (weekDate.isBefore(now)) {
            throw new IllegalArgumentException("Geçmiş haftalardaki seanslar görüntülenemez");
        }

        if (weekDate.isAfter(fourWeeksFromNow)) {
            throw new IllegalArgumentException("5 haftadan daha ileri tarihli seanslar görüntülenemez");
        }

        LocalDateTime startOfWeek = weekDate.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime endOfWeek = weekDate.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
        List<Session> sessions = sessionRepository.findByDateBetween(startOfWeek, endOfWeek);
        return sessions.stream()
                .filter(session -> session.getSessionStatus() != SessionStatusType.CANCELED)
                .map(session -> {
                    LocalDateTime date = session.getDate();
                    session.setDate(date.withMinute(0).withSecond(0).withNano(0));
                    return session;
                })
                .toList();
    }
}
