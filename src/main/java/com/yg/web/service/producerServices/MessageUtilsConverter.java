package com.yg.web.service.producerServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class MessageUtilsConvertor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeMessage(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message: " + message, e);
        }
    }
}