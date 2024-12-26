package com.hburak_dev.psk_full_stack.session;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleMeetService {

  private final Calendar calendarService;

  @Value("${google.calendar.therapist-email}")
  private String therapistEmail;

  public void createMeeting(Session session) {
    try {
      Event event = new Event()
          .setSummary("Psikolojik Danışmanlık Seansı")
          .setDescription("Danışan: " + session.getUser().getFullName());

      // Fix the date format to RFC3339
      String startTimeStr = session.getDate()
          .atZone(ZoneId.of("Europe/Istanbul"))
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
      DateTime startDateTime = new DateTime(startTimeStr);

      EventDateTime start = new EventDateTime()
          .setDateTime(startDateTime)
          .setTimeZone("Europe/Istanbul");
      event.setStart(start);

      // Fix end time format as well
      String endTimeStr = session.getDate()
          .plusMinutes(50)
          .atZone(ZoneId.of("Europe/Istanbul"))
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
      DateTime endDateTime = new DateTime(endTimeStr);

      EventDateTime end = new EventDateTime()
          .setDateTime(endDateTime)
          .setTimeZone("Europe/Istanbul");
      event.setEnd(end);

      // Add Google Meet conference
      ConferenceData conferenceData = new ConferenceData()
          .setCreateRequest(new CreateConferenceRequest()
              .setRequestId(session.getId().toString())
              .setConferenceSolutionKey(new ConferenceSolutionKey()
                  .setType("hangoutsMeet")));
      event.setConferenceData(conferenceData);

      // Add both therapist and client as attendees
      EventAttendee[] attendees = new EventAttendee[] {
          new EventAttendee().setEmail(therapistEmail).setOrganizer(true),
          new EventAttendee().setEmail(session.getUser().getEmail())
      };
      event.setAttendees(Arrays.asList(attendees));

      // Create event with Google Meet
      Event createdEvent = calendarService.events().insert("primary", event)
          .setConferenceDataVersion(1)
          .execute();

      // Store event ID and return meet link
      session.setGoogleEventId(createdEvent.getId());
      session.setSessionLink(createdEvent.getHangoutLink());

    } catch (IOException e) {
      log.error("Error creating Google Meet", e);
      throw new RuntimeException("Failed to create Google Meet link", e);
    }
  }

  public void deleteMeeting(String eventId) {
    if (eventId == null)
      return;

    try {
      calendarService.events().delete("primary", eventId).execute();
      log.info("Meeting deleted: {}", eventId);
    } catch (IOException e) {
      log.error("Error deleting Google Meet", e);
      throw new RuntimeException("Failed to delete Google Meet", e);
    }
        }
}
