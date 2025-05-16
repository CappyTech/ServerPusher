package com.sovereigncraft.serverpusher;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class ServerPusher {

    private static String backendUrl;
    private static Map<String, String> customHeaders;
    private static Logger logger = Logger.getLogger("ServerPusher");
    private static final Gson gson = new Gson();

    public static void configure(String url, Map<String, String> headers, Logger log) {
        backendUrl = url;
        customHeaders = headers;
        logger = log;
    }

    public static boolean sendData(String event, Map<String, Object> payload) {
        if (backendUrl == null) {
            logger.warning("ServerPusher not configured. Call configure() first.");
            return false;
        }

        try {
            payload.put("event", event);
            payload.put("timestamp", Instant.now().toString());

            String json = gson.toJson(payload);
            URL url = URI.create(backendUrl).toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            if (customHeaders != null) {
                for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            if (code != 200) {
                logger.warning("ServerPusher failed to push: " + code);
                return false;
            }

            return true;

        } catch (Exception e) {
            logger.warning("ServerPusher error: " + e.getMessage());
            return false;
        }
    }
} 
