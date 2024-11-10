package com.hburak_dev.psk_full_stack.session;


import com.hburak_dev.psk_full_stack.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionService {

    List<PublicSessionResponse> getAllSessionsWeekly(LocalDateTime week);

    List<SessionResponse> getMySessions(Authentication connectedUser);

    Integer createUserSession(LocalDateTime date, Authentication connectedUser);

    Integer createSessionForUserV2(LocalDateTime date, Integer userId);

    ResponseEntity<Boolean> cancelUserSession(Integer id, Authentication connectedUser);

    PageResponse<SessionResponseV2> getAllSessionsV2(int page, int size);

    SessionResponseV2 addNoteToSessionForUserV2(SessionUserNoteRequest sessionNoteRequest);

    SessionResponseV2 addNoteToSessionForPsychologistV2(SessionPsychologistNoteRequest sessionNoteRequest);

    Integer updateSessionStatusV2(SessionStatusRequest sessionStatusRequest);

    Integer updateSessionDateV2(SessionDateRequest sessionStatusRequest);

    PageResponse<UserWithSessionResponse> getAllUsersWithSessionV2(int page, int size);

    List<PublicSessionResponse> makeUnavailableV2(List<LocalDateTime> unavailableTimes);

    List<PublicSessionResponse> makeAvailableV2(List<LocalDateTime> availableTimes);

    SessionResponseV2 updateSessionPaidStatusV2(Boolean isPaid, Integer sessionId);

    List<SessionResponseV2> getAllSessionsOfUserV2(Integer userId);
}
