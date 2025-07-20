package com.seemee.userservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomObjectMapper {
    public static ObjectMapper createCustomMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new ObjectIdDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
