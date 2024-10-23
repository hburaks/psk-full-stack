package com.hburak_dev.psk_full_stack.session;


import com.hburak_dev.psk_full_stack.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

public interface SessionService {

    PublicSessionResponse getAllSessionsWeekly(LocalDateTime week);

    PageResponse<SessionResponse> getMySessions(Authentication connectedUser);

    Integer createUserSession(UserSessionRequest userSessionRequest, Authentication connectedUser);

    ResponseEntity<Boolean> cancelUserSession(Integer id, Authentication connectedUser);

    PageResponse<SessionResponse> getAllSessionsV2(Authentication connectedUser);

    SessionResponse addNoteToSessionV2(SessionNoteRequest sessionNoteRequest, Authentication connectedUser);

    Integer updateSessionStatusV2(SessionStatusRequest sessionStatusRequest, Authentication connectedUser);

    Integer updateSessionDateV2(SessionDateRequest sessionStatusRequest, Authentication connectedUser);

    PageResponse<UserWithSessionResponse> getAllUsersWithSessionV2(int page, int size, Authentication connectedUser);

    PublicSessionResponse makeUnavailable(UserSessionRequest userSessionRequest, Authentication connectedUser);

    PublicSessionResponse makeAvailable(UserSessionRequest userSessionRequest, Authentication connectedUser);

}
