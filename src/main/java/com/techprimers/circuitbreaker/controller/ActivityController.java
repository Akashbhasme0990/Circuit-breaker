package com.techprimers.circuitbreaker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@RequestMapping("/activity")
@RestController
public class ActivityController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String ADVICE_API = "https://api.adviceslip.com/advice";

    public ActivityController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/getAdvice")
    @CircuitBreaker(name = "randomActivity", fallbackMethod = "fallbackRandomActivity")
    public String getRandomActivity() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ADVICE_API, String.class);
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseEntity.getBody());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response", e);
        }
        String advice = jsonNode.path("slip").path("advice").asText();
        log.info("Advice received: " + advice);
        return advice;
    }

    @GetMapping("type/{type}")
    @CircuitBreaker(name = "activityByType", fallbackMethod = "fallbackActivityByType")
    public String getActivityByType(@PathVariable String type) {
        String url = ADVICE_API + "?type=" + type;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseEntity.getBody());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response", e);
        }
        String advice = jsonNode.path("slip").path("advice").asText();
        log.info("Advice received: " + advice);
        return advice;
    }

    public String fallbackRandomActivity(Throwable throwable) {
        return "Watch a video from TechPrimers";
    }
    public String fallbackActivityByType(String type, Throwable throwable) {
        return "Try an indoor activity like reading a book";
    }
}

