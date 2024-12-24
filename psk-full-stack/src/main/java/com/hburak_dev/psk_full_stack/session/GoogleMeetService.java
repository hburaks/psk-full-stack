package com.hburak_dev.psk_full_stack.session;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GoogleMeetService {

  private static final String MEET_BASE_URL = "https://meet.google.com/";

  public String generateMeetLink() {
    // Generate a random 10-character string for the meeting ID
    String meetingId = generateRandomString(10);
    return MEET_BASE_URL + meetingId;
  }

  private String generateRandomString(int length) {
    String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }

    return sb.toString();
  }
}