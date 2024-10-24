package com.hburak_dev.psk_full_stack.session;


import com.hburak_dev.psk_full_stack.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionService {

    List<PublicSessionResponse> getAllSessionsWeekly(LocalDateTime week);

    PageResponse<SessionResponse> getMySessions(int page, int size, Authentication connectedUser);

    Integer createUserSession(UserSessionRequest userSessionRequest, Authentication connectedUser);

    ResponseEntity<Boolean> cancelUserSession(Integer id, Authentication connectedUser);

    PageResponse<SessionResponseV2> getAllSessionsV2(Authentication connectedUser);

    SessionResponseV2 addNoteToSessionForUserV2(SessionUserNoteRequest sessionNoteRequest, Authentication connectedUser);

    SessionResponseV2 addNoteToSessionForPsychologistV2(SessionPsychologistNoteRequest sessionNoteRequest, Authentication connectedUser);

    Integer updateSessionStatusV2(SessionStatusRequest sessionStatusRequest, Authentication connectedUser);

    Integer updateSessionDateV2(SessionDateRequest sessionStatusRequest, Authentication connectedUser);

    PageResponse<UserWithSessionResponse> getAllUsersWithSessionV2(int page, int size, Authentication connectedUser);

    SessionResponseV2 makeUnavailableV2(SessionAvailabilityRequest sessionAvailabilityRequest, Authentication connectedUser);

    SessionResponseV2 makeAvailableV2(SessionAvailabilityRequest sessionAvailabilityRequest, Authentication connectedUser);

}
