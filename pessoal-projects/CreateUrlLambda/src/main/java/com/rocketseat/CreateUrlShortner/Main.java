package com.rocketseat.CreateUrlShortner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    Region region = Region.US_EAST_1;
    private final S3Client s3Client = S3Client.builder().region(region).build();

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {
        String body = input.get("body").toString();

        Map<String, String> bodyMap;
        try{
            bodyMap = objectMapper.readValue(body, Map.class);
        }catch (Exception e){
            throw  new RuntimeException("Error parsing JSON" + e.getMessage(), e);
        }
        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");
        long expirationTimeSec = Long.parseLong(expirationTime);

        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

        UrlData urlData = new UrlData(originalUrl, expirationTimeSec);
        try {
            String urlDataJSON = objectMapper.writeValueAsString(urlData);

            PutObjectRequest request = PutObjectRequest.builder().bucket("my-first-bucket-for-url-shortener-lambda").key(shortUrlCode + ".json").build();

            s3Client.putObject(request, RequestBody.fromString(urlDataJSON));

        }catch (Exception e) {
            throw new RuntimeException("Error saving url data to S3" + e.getMessage(), e);
        }
        Map<String, String> response = new HashMap<>();
        response.put("statusCode", shortUrlCode);

        return response;
    }

    /*public static void main(String[] args) {
        Main main = new Main();
        Map<String, Object> input = new HashMap<>();
        input.put("body", "{\"originalUrl\": \"https://www.google.com\", \"expirationTime\": \"111111\"}");
        Map<String, String> result = main.handleRequest(input, null);
        System.out.println(result);

    }*/
}