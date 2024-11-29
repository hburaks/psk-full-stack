package com.hburak_dev.psk_full_stack.session;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HourlySessionResponse {

  private LocalDateTime date;

  private boolean isAvailable;

}
