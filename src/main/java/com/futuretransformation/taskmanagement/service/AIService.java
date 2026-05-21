package com.futuretransformation.taskmanagement.service;

import com.futuretransformation.taskmanagement.dto.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_URL =
        "https://api.groq.com/openai/v1/chat/completions";

    public AIResponse generateTaskDetails(String title) {
        try {
            String prompt = "You are a project management assistant. "
                + "Given a task title, respond in EXACTLY this format with no extra text:\\n"
                + "DESCRIPTION: <one sentence description>\\n"
                + "PRIORITY: <LOW or MEDIUM or HIGH>\\n"
                + "EFFORT: <estimated time like 2 hours or 1 day>\\n"
                + "Task title: " + title;

            String requestBody = "{"
                + "\"model\": \"llama-3.3-70b-versatile\","
                + "\"messages\": [{"
                + "\"role\": \"user\","
                + "\"content\": \"" + prompt + "\""
                + "}],"
                + "\"temperature\": 0.7"
                + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("Groq raw response: " + response.body());

            return parseGroqResponse(response.body());

        } catch (Exception e) {
            System.out.println("AI error: " + e.getMessage());
            return new AIResponse(
                "Complete the task: " + title,
                "MEDIUM",
                "2 hours"
            );
        }
    }

    private AIResponse parseGroqResponse(String responseBody) {
        try {
            String contentMarker = "\"content\":\"";
            int contentStart = responseBody.indexOf(contentMarker) + contentMarker.length();
            int contentEnd = responseBody.indexOf("\"}", contentStart);

            String rawText = responseBody.substring(contentStart, contentEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"");

            System.out.println("Parsed text: " + rawText);

            String description = "No description generated";
            String priority = "MEDIUM";
            String effort = "2 hours";

            for (String line : rawText.split("\n")) {
                line = line.trim();
                if (line.startsWith("DESCRIPTION:")) {
                    description = line.substring("DESCRIPTION:".length()).trim();
                } else if (line.startsWith("PRIORITY:")) {
                    priority = line.substring("PRIORITY:".length()).trim();
                } else if (line.startsWith("EFFORT:")) {
                    effort = line.substring("EFFORT:".length()).trim();
                }
            }

            return new AIResponse(description, priority, effort);

        } catch (Exception e) {
            System.out.println("Parse error: " + e.getMessage());
            return new AIResponse(
                "Complete the task",
                "MEDIUM",
                "2 hours"
            );
        }
    }
}