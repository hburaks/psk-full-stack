package com.hburak_dev.psk_full_stack.common;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.hburak_dev.psk_full_stack.session.Session;
import com.hburak_dev.psk_full_stack.email.EmailService;
import com.hburak_dev.psk_full_stack.email.EmailTemplateName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleMeetService {

  private final Calendar calendarService;
  private final EmailService emailService;

  @Value("${google.calendar.therapist-email}")
  private String therapistEmail;

  public void createMeeting(Session session) {
    try {
      Event event = new Event()
          .setSummary("Psikoterapi Seansı")
          .setDescription("Danışan: " + session.getUser().getFullName());

      String startTimeStr = session.getDate()
          .minusHours(3)
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
      DateTime startDateTime = new DateTime(startTimeStr);

      EventDateTime start = new EventDateTime()
          .setDateTime(startDateTime);
      event.setStart(start);

      String endTimeStr = session.getDate()
          .minusHours(3)
          .plusMinutes(50)
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
      DateTime endDateTime = new DateTime(endTimeStr);

      EventDateTime end = new EventDateTime()
          .setDateTime(endDateTime);
      event.setEnd(end);

      ConferenceData conferenceData = new ConferenceData()
          .setCreateRequest(new CreateConferenceRequest()
              .setRequestId(session.getId().toString())
              .setConferenceSolutionKey(new ConferenceSolutionKey()
                  .setType("hangoutsMeet")));
      event.setConferenceData(conferenceData);

      EventAttendee[] attendees = new EventAttendee[] {
          new EventAttendee().setEmail(therapistEmail).setOrganizer(true),
          new EventAttendee().setEmail(session.getUser().getEmail())
      };
      event.setAttendees(Arrays.asList(attendees));

      // Add email reminders
      EventReminder[] reminderOverrides = new EventReminder[] {
          new EventReminder().setMethod("email").setMinutes(24 * 60), // 1 day before
          new EventReminder().setMethod("email").setMinutes(60) // 1 hour before
      };

      Event.Reminders reminders = new Event.Reminders()
          .setUseDefault(false)
          .setOverrides(Arrays.asList(reminderOverrides));
      event.setReminders(reminders);

      // Create event with Google Meet
      Event createdEvent = calendarService.events().insert("primary", event)
          .setConferenceDataVersion(1)
          .execute();

      // Store event details
      session.setGoogleEventId(createdEvent.getId());
      session.setSessionLink(createdEvent.getHangoutLink());

      // Send immediate confirmation emails
      sendSessionEmails(session, createdEvent.getHangoutLink());

    } catch (IOException e) {
      log.error("Error creating Google Meet", e);
      throw new RuntimeException("Failed to create Google Meet link", e);
    }
  }

  private void sendSessionEmails(Session session, String meetLink) {
    try {
      String sessionDateStr = session.getDate()
          .format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")); // Match the expected format

      // Send to user
      emailService.sendEmail(
          session.getUser().getEmail(),
          session.getUser().getFullName(),
          EmailTemplateName.SESSION_REMINDER,
          meetLink,
          null,
          "Seans Randevunuz Oluşturuldu",
          sessionDateStr);

      // Send to therapist
      emailService.sendEmail(
          therapistEmail,
          "Terapist",
          EmailTemplateName.SESSION_REMINDER,
          meetLink,
          null,
          "Yeni Seans Randevusu",
          sessionDateStr);
    } catch (Exception e) {
      log.error("Error sending session emails", e);
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
