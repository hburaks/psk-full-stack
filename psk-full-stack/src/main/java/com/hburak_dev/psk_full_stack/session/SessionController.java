package com.hburak_dev.psk_full_stack.session;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;


    @GetMapping("/my-sessions")
    public List<SessionResponse> getMySessions(Authentication connectedUser) {
        return sessionService.getMySessions(connectedUser);
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> createSessionForUserV2(
            @RequestParam("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH") String date, Authentication connectedUser) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");

        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);

        Integer sessionId = sessionService.createUserSession(localDateTime, connectedUser);
        return ResponseEntity.ok(sessionId);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Boolean> cancelUserSession(@PathVariable Integer id, Authentication connectedUser) {
        return sessionService.cancelUserSession(id, connectedUser);
    }

    @GetMapping("/upcoming-session")
    public ResponseEntity<SessionResponse> getUpcomingSession(Authentication connectedUser) {
        SessionResponse sessionResponse = sessionService.getUpcomingSession(connectedUser);
        if (sessionResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sessionResponse);
    }

}
