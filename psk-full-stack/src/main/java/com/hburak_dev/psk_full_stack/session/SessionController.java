package com.hburak_dev.psk_full_stack.session;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<Integer> createMySession(
            @RequestParam("date")
            LocalDateTime date, Authentication connectedUser) {

        Integer sessionId = sessionService.createUserSession(date, connectedUser);
        return ResponseEntity.ok(sessionId);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Boolean> cancelUserSession(@PathVariable Integer id, Authentication connectedUser) {
        return sessionService.cancelUserSession(id, connectedUser);
    }

    @GetMapping("/upcoming-session")
    public ResponseEntity<SessionResponse> getUpcomingSession(Authentication connectedUser) {
        SessionResponse sessionResponse = sessionService.getUpcomingSession(connectedUser);
        return ResponseEntity.ok(sessionResponse);
    }

}
