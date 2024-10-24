package com.hburak_dev.psk_full_stack.session;

import org.springframework.stereotype.Service;

@Service
public class SessionMapper {


    public PublicSessionResponse toPublicSessionResponse(Session session) {

        return PublicSessionResponse.builder()
                .date(session.getDate())
                .build();
    }
}
