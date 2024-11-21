package com.rocketseat.CreateUrlShortner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {
        String body = (String) input.get("body");

        Map<String, String> bodyMap;
        try{
            bodyMap = objectMapper.readValue(body, Map.class);
        }catch (Exception e){
            throw  new RuntimeException("Error parsing JSON" + e.getMessage(), e);
        }
        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");

        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

        Map<String, String> response = new HashMap<String, String>();
        response.put("statusCode", shortUrlCode);

        return response;
    }
}