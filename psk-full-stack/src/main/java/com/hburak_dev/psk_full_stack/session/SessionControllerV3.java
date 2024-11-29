package com.hburak_dev.psk_full_stack.session;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("v3/sessions")
@RequiredArgsConstructor
public class SessionControllerV3 {

    private final SessionService sessionService;

    @GetMapping("/weekly-calendar")
    public List<DailyCalendarResponse> getWeeklyCalendar(@RequestParam("dateTime") LocalDateTime dateTime) {
        return sessionService.getWeeklyCalendar(dateTime);
    }

}
