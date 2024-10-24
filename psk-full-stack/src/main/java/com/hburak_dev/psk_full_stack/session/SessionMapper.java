package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.user.User;
import org.springframework.stereotype.Service;

@Service
public class SessionMapper {


    public PublicSessionResponse toPublicSessionResponse(Session session) {

        return PublicSessionResponse.builder()
                .date(session.getDate())
                .build();
    }

    public SessionResponse toSessionResponse(Session session) {

        return SessionResponse.builder()
                .date(session.getDate())
                .sessionStatus(session.getSessionStatus())
                .sessionId(session.getId())
                .noteForUser(session.getNoteForUser())
                .build();
    }

    public Session toSession(UserSessionRequest userSessionRequest, User user) {
        return Session.builder()
                .date(userSessionRequest.getDate())
                .user(user)
                .sessionStatus(SessionStatusType.AWAITING_PSYCHOLOGIST_APPROVAL)
                .isSessionPaid(false)
                .build();
    }
}
