package com.podflix.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class YouTubeConfig {

    @Value("${youtube.application.name}")
    private String applicationName;

    @Bean
    public YouTube youTube() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new YouTube.Builder(httpTransport, jsonFactory, request -> {
        })
                .setApplicationName(applicationName)
                .build();
    }
}
