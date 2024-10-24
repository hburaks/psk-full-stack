package com.hburak_dev.psk_full_stack.session;

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
}
