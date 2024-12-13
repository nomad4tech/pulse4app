package tech.nomad4.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.nomad4.model.Pulse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PulseSender {

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void pulse(String url, Map<String, String> headers, Pulse response) {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(getJson(response)))
                .header("Content-Type", "application/json");

        if (headers != null)
            headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> resp =  httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            checkStatus(url, resp);
        } catch (Exception e)  {
            log.error("Request on {} failed with message: {}", url, e.getMessage());
        }
    }

    private String getJson(Pulse response) {
        String json;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    private static void checkStatus(String url, HttpResponse<String> resp) {
        int statusCode = resp.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            log.error("Request on {} failed with status code: {}", url, statusCode);
        }
    }

    public void pulse(String url, Pulse response) {
        pulse(url, null, response);
    }

}
