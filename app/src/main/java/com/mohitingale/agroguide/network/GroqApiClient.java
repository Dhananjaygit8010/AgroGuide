package com.mohitingale.agroguide.network;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

public class GroqApiClient {
    private static final String TAG = "GroqApiClient";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface GroqCallback {
        void onSuccess(String jsonResult);
        void onError(String error);
    }

    public void analyzeCrop(Bitmap bitmap, String apiKey, GroqCallback callback) {
        executor.execute(() -> {
            try {
                // Convert bitmap to Base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                // Build Request JSON payload matching OpenAI specification
                JSONObject payload = new JSONObject();
                payload.put("model", "meta-llama/llama-4-scout-17b-16e-instruct");
                
                JSONArray messages = new JSONArray();
                JSONObject userMessage = new JSONObject();
                userMessage.put("role", "user");

                JSONArray contentArray = new JSONArray();
                
                JSONObject textContent = new JSONObject();
                textContent.put("type", "text");
                textContent.put("text", "You are an expert agronomist. Analyze this crop leaf image for diseases. " +
                        "Identify the disease name, scientific name or infection type, risk level (use only 'ACTION REQUIRED', 'MONITORING', or 'HEALTHY'), " +
                        "confidence percentage (e.g. 92%), severity level (High, Medium, Low), spread rate (High, Medium, Low), estimated recovery time (e.g. 12 Days), " +
                        "organic remedy title and description, chemical remedy title and description, and prevention tips. " +
                        "You MUST return ONLY a clean JSON object matching this exact schema: {\n" +
                        "  \"disease_name\": \"...\",\n" +
                        "  \"scientific_name\": \"...\",\n" +
                        "  \"confidence_percentage\": \"...%\",\n" +
                        "  \"risk_level\": \"...\",\n" +
                        "  \"health_score\": \"...\",\n" +
                        "  \"spread_rate\": \"...\",\n" +
                        "  \"recovery_time\": \"...\",\n" +
                        "  \"organic_remedy_title\": \"...\",\n" +
                        "  \"organic_remedy_desc\": \"...\",\n" +
                        "  \"chemical_remedy_title\": \"...\",\n" +
                        "  \"chemical_remedy_desc\": \"...\",\n" +
                        "  \"prevention_tips\": \"...\"\n" +
                        "}. Do not include markdown formatting or backticks around the JSON. Return only the raw JSON object.");
                contentArray.put(textContent);

                JSONObject imageContent = new JSONObject();
                imageContent.put("type", "image_url");
                JSONObject imageUrlObj = new JSONObject();
                imageUrlObj.put("url", "data:image/jpeg;base64," + base64Image.trim().replace("\n", ""));
                imageContent.put("image_url", imageUrlObj);
                contentArray.put(imageContent);

                userMessage.put("content", contentArray);
                messages.put(userMessage);
                payload.put("messages", messages);

                // Set up HTTP Connection to Groq Endpoint
                URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setDoOutput(true);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);

                // Write request JSON body
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Read HTTP Response
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    java.util.Scanner s = new java.util.Scanner(conn.getInputStream()).useDelimiter("\\A");
                    String response = s.hasNext() ? s.next() : "";
                    
                    JSONObject responseJson = new JSONObject(response);
                    String aiMessageContent = responseJson.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    callback.onSuccess(aiMessageContent);
                } else {
                    java.util.Scanner s = new java.util.Scanner(conn.getErrorStream()).useDelimiter("\\A");
                    String errorResponse = s.hasNext() ? s.next() : "";
                    callback.onError("HTTP " + responseCode + ": " + errorResponse);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error analyzing crop with Groq", e);
                callback.onError(e.getMessage());
            }
        });
    }
}
