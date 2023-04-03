package com.bluepantsmedia.ids.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/21/2022 4:50 PM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class JsonUtils {
    private JsonUtils() {
    }

    ;

    public static void printJsonFile(String file) {
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            String content = Files.readString(Paths.get(file));

            if (HashUtils.isJSONValid(content)) {
                JsonNode root = mapper.readTree(content);
                System.out.println("root = " + root.toString());
            }

            // convert JSON file to map
            Map<?, ?> map = mapper.readValue(Paths.get(file).toFile(), Map.class);

            // print map entries
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                String key = (String) entry.getKey();
                System.out.print(key + " = ");
                if (entry.getValue() instanceof String) {
                    System.out.println(entry.getValue());
                } else {
                    if (value instanceof String) {
                        System.out.println(value);
                    } else if (HashUtils.isNumeric(value)) {
                        System.out.println(Double.parseDouble((String) value));
                    } else if (value instanceof Map) {
                        Map<String, Object> subMap = (Map<String, Object>) value;
                        HashUtils.hashMapper(subMap);
                    } else if (HashUtils.isCharArray(value)) {
                        System.out.println(String.valueOf(value));
                    } else {
                        throw new IllegalArgumentException(String.valueOf(value));
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
