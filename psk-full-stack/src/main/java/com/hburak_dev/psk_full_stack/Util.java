package com.hburak_dev.psk_full_stack;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static List<LocalDateTime> toFullHours(List<LocalDateTime> dateTimes) {
        return dateTimes.stream()
                .map(Util::toFullHour)
                .collect(Collectors.toList());
    }

    public static LocalDateTime toFullHour(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
    }

}
