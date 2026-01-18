package com.example.PayoEat_BE.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors.allowed-origins:}")
    private String additionalOrigins;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        List<String> origins = new ArrayList<>(List.of(
                "https://m8t547vr-5173.asse.devtunnels.ms",
                "http://localhost:5173"
        ));

        if (additionalOrigins != null && !additionalOrigins.isBlank()) {
            origins.addAll(Arrays.asList(additionalOrigins.split(",")));
        }

        registry.addEndpoint("/ws")
                .setAllowedOrigins(origins.toArray(new String[0]))
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
