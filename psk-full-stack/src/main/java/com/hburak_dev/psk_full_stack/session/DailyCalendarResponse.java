
package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.DayOfWeek;

import java.util.List;

@Getter
@Setter
@Builder
public class DailyCalendarResponse {

  private DayOfWeek dayOfWeek;

  private List<HourlySessionResponse> sessions;

}