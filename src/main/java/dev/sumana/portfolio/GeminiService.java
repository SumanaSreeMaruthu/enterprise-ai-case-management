package dev.sumana.portfolio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class GeminiService {

    private final ObjectMapper mapper;
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public GeminiService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String summarize(String notes) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Set GEMINI_API_KEY to enable AI summaries");
        }
        try {
            String prompt = "Summarize these synthetic investigation notes. "
                    + "Do not make compliance decisions; mention uncertainty. Notes: " + notes;
            String payload = mapper.writeValueAsString(Map.of(
                    "contents", new Object[] { Map.of(
                            "parts", new Object[] { Map.of("text", prompt) }) }));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"))
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Gemini request failed with HTTP " + response.statusCode());
            }
            JsonNode text = mapper.readTree(response.body())
                    .path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (!text.isTextual()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No text returned by Gemini");
            }
            return text.asText();
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI service unavailable", exception);
        }
    }
}
