package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                                .sessionLink(session.getSessionLink())
                                .build();
        }

        public Session toSession(LocalDateTime date, User user) {
                return Session.builder()
                                .date(date)
                                .user(user)
                                .sessionStatus(SessionStatusType.AWAITING_THERAPIST_APPROVAL)
                                .isSessionPaid(false)
                                .isMock(false)
                                .build();
        }

        public SessionResponseV2 toSessionResponseV2(Session session) {
                return SessionResponseV2.builder()
                                .sessionId(session.getId())
                                .userForSessionResponse(
                                                UserForSessionResponse.builder()
                                                                .id(session.getUser().getId())
                                                                .firstname(session.getUser().getFirstname())
                                                                .lastname(session.getUser().getLastname())
                                                                .build())
                                .date(session.getDate())
                                .sessionStatus(session.getSessionStatus())
                                .noteForUser(session.getNoteForUser())
                                .noteForPsychologist(session.getNoteForPsychologist())
                                .isPaid(session.isSessionPaid())
                                .sessionLink(session.getSessionLink())
                                .build();
        }


        public List<Integer> toSessionIdList(List<Session> sessions) {
                return sessions.stream()
                                .map(Session::getId)
                                .collect(Collectors.toList());
        }

}
