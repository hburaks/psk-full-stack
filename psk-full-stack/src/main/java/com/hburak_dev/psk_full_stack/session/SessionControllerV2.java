package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("v2/sessions")
@RequiredArgsConstructor
public class SessionControllerV2 {

    private final SessionService sessionService;

    @GetMapping("/all")
    public PageResponse<SessionResponseV2> getAllSessionsV2(@RequestParam int page, @RequestParam int size) {
        return sessionService.getAllSessionsV2(page, size);
    }

    @PostMapping("/add-note/user")
    public SessionResponseV2 addNoteToSessionForUserV2(@RequestBody SessionUserNoteRequest sessionNoteRequest) {
        return sessionService.addNoteToSessionForUserV2(sessionNoteRequest);
    }

    @PostMapping("/add-note/psychologist")
    public SessionResponseV2 addNoteToSessionForPsychologistV2(@RequestBody SessionPsychologistNoteRequest sessionNoteRequest) {
        return sessionService.addNoteToSessionForPsychologistV2(sessionNoteRequest);
    }

    @PostMapping("/update-status")
    public Integer updateSessionStatusV2(@RequestBody SessionStatusRequest sessionStatusRequest) {
        return sessionService.updateSessionStatusV2(sessionStatusRequest);
    }

    @PostMapping("/update-date")
    public Integer updateSessionDateV2(@RequestBody SessionDateRequest sessionDateRequest) {
        return sessionService.updateSessionDateV2(sessionDateRequest);
    }

    @GetMapping("/users-with-sessions")
    public PageResponse<UserWithIncomingSessionResponse> getAllUsersWithSessionV2(@RequestParam int page, @RequestParam int size) {
        return sessionService.getAllUsersWithSessionV2(page, size);
    }

    @PostMapping("/make-unavailable")
    public List<PublicSessionResponse> makeUnavailableV2(@RequestBody List<LocalDateTime> unavailableTimes) {
        return sessionService.makeUnavailableV2(unavailableTimes);
    }

    @PostMapping("/make-available")
    public List<PublicSessionResponse> makeAvailableV2(@RequestBody List<LocalDateTime> availableTimes) {
        return sessionService.makeAvailableV2(availableTimes);
    }

    @PostMapping("/update-paid-status")
    public SessionResponseV2 updateSessionPaidStatusV2(@RequestParam Boolean isPaid, @RequestParam Integer sessionId) {
        return sessionService.updateSessionPaidStatusV2(isPaid, sessionId);
    }

    @GetMapping("/user/{userId}")
    public List<SessionResponseV2> getAllSessionsOfUserV2(@PathVariable Integer userId) {
        return sessionService.getAllSessionsOfUserV2(userId);
    }

    @PostMapping("/create")
    public Integer createSessionForUserV2(
            @RequestParam("date") LocalDateTime date, @RequestParam Integer userId) {

        return sessionService.createSessionForUserV2(date, userId);
    }

    @GetMapping("/upcoming-session")
    public SessionResponseV2 getUpcomingSessionsV2() {
        return sessionService.getUpcomingSessionsV2();
    }


}
