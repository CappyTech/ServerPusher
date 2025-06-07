package com.sovereigncraft.serverpusher;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class ServerPusher {

    private static String backendUrl;
    private static Map<String, String> customHeaders;
    private static Logger logger = Logger.getLogger("ServerPusher");
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void configure(String url, Map<String, String> headers, Logger log) {
        backendUrl = url;
        customHeaders = headers;
        logger = log;
    }

    public static void sendData(String event, Map<String, Object> payload) {
        if (backendUrl == null) {
            logger.warning("ServerPusher not configured. Call configure() first.");
            return;
        }

        payload.put("event", event);
        payload.put("timestamp", Instant.now().toString());

        String json = gson.toJson(payload);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(backendUrl))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));

        if (customHeaders != null) {
            for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest request = requestBuilder.build();

        long start = System.nanoTime();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
            .thenAccept(response -> {
                long durationMs = (System.nanoTime() - start) / 1_000_000;

                int statusCode = response.statusCode();
                String body = response.body();
                HttpHeaders headers = response.headers();

                if (statusCode == 200) {
                    logger.info("[ServerPusher] Success (" + durationMs + " ms): " + body);
                } else {
                    logger.warning("[ServerPusher] Failed (" + statusCode + ", " + durationMs + " ms): " + body);
                }

                logger.fine("[ServerPusher] Response Headers: " + headers.map());
            })
            .exceptionally(e -> {
                long durationMs = (System.nanoTime() - start) / 1_000_000;
                logger.warning("[ServerPusher] Exception after " + durationMs + " ms: " + e.getMessage());
                return null;
            });
    }
}
