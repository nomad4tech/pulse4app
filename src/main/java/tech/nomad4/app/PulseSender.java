package tech.nomad4.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.nomad4.model.Pulse;
import tech.nomad4.model.PulseResponse;

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

    private boolean connected = false;

    public void pulse(String url, Map<String, String> headers, Pulse pulse) {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(getJson(pulse)))
                .header("Content-Type", "application/json");

        if (headers != null)
            headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> resp =  httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            tryCheckStatus(resp);
            PulseResponse pr = objectMapper.readValue(resp.body(), PulseResponse.class);

            // TODO can be null, rework for check and update connection status
            if (pr != null && !connected) {
                log.info("Listener Connected: {}", url);
                connected = true;
            }
        } catch (Exception e) {
            connected = false;
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

    private void tryCheckStatus(HttpResponse<String> resp) {
        int statusCode = resp.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException("Request status code:" + statusCode);
        }
    }

    public void pulse(String url, Pulse response) {
        pulse(url, null, response);
    }

}
