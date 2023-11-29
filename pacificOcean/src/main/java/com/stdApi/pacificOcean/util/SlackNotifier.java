package com.stdApi.pacificOcean.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class SlackNotifier {

    @Value("${slack.url}")
    private String url;
    private RestTemplate restTemplate;

    public SlackNotifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendErrorNotification(String errorMessage) {
        String payload = "{\"channel\": \"#pacificocean\", \"username\": \"webhookbot\", \"text\": \"" + errorMessage + "\", \"icon_emoji\": \":ghost:\"}";
        restTemplate.postForEntity(url, payload, String.class);
    }
}