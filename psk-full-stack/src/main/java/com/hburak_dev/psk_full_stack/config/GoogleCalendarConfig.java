package com.hburak_dev.psk_full_stack.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GoogleCalendarConfig {

    @Value("${google.calendar.client-id}")
    private String clientId;

    @Value("${google.calendar.client-secret}")
    private String clientSecret;

    @Value("${google.calendar.refresh-token}")
    private String refreshToken;

    @Value("${google.calendar.application-name}")
    private String applicationName;

    @Bean
    public Calendar googleCalendarService() throws Exception {
        GoogleCredential credential = new GoogleCredential.Builder()
            .setClientSecrets(clientId, clientSecret)
            .setTransport(GoogleNetHttpTransport.newTrustedTransport())
            .setJsonFactory(GsonFactory.getDefaultInstance())
            .build()
            .setRefreshToken(refreshToken);

        return new Calendar.Builder(
            GoogleNetHttpTransport.newTrustedTransport(), 
            GsonFactory.getDefaultInstance(), 
            credential)
            .setApplicationName(applicationName)
            .build();
    }

    @PostConstruct
    public void testToken() {
        try {
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setClientSecrets(clientId, clientSecret)
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(GsonFactory.getDefaultInstance())
                    .build()
                    .setRefreshToken(refreshToken);

            boolean success = credential.refreshToken();
            System.out.println("REFRESH RESULT: " + success);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 