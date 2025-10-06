package com.example.Bajaj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class runner implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.name}")
    private String name;

    @Value("${app.regNo}")
    private String regNo;

    @Value("${app.email}")
    private String email;

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public runner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*@Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application started. Generating webhook...");
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("regNo", regNo);
        requestBody.put("email", email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response =
                restTemplate.postForEntity(GENERATE_WEBHOOK_URL, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to generate webhook: " + response.getStatusCode());
            return;
        }
        JsonNode json = objectMapper.readTree(response.getBody());
        String webhookUrl = json.get("webhook").asText();
        String accessToken = json.get("accessToken").asText();
        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("Access Token received.");
        String sqlQuery = solveSqlQuestion(regNo);
        submitSolution(webhookUrl, accessToken, sqlQuery);
    }*/

    private String solveSqlQuestion(String regNo) {
        String digits = regNo.replaceAll("\\D+", "");
        String lastTwo = digits.substring(Math.max(0, digits.length() - 2));
        int n = Integer.parseInt(lastTwo);
        if (n % 2 == 1) {
            System.out.println("Odd RegNo detected ⇒ Using Question 1");
            return "SELECT * FROM employees WHERE salary > 50000;";
        }
        else {
            System.out.println("Even RegNo detected ⇒ Using Question 2");
            return "SELECT department, COUNT(*) FROM employees GROUP BY department;";
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", sqlQuery);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(webhookUrl, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Solution submitted successfully!");
                System.out.println("Response: " + response.getBody());
            }
            else {
                System.err.println("Failed to submit solution: " + response.getStatusCode());
                System.err.println("Response: " + response.getBody());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Autowired
    private Websql websql;

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> result = websql.findHighestSalaryExcludingFirstDay();
        System.out.println("---- Query Result ----");
        System.out.println(result);
    }
}
