package com.bluepantsmedia.ids.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/21/2022 4:02 PM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class HashUtils {
    private HashUtils() {
    }

    ;

    public static void hashMapper(Map<String, Object> lhm1) throws ParseException {
        for (Map.Entry<String, Object> entry : lhm1.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                System.out.println(value);
            } else if (isNumeric(value)) {
                System.out.println(Double.parseDouble((String) value));
            } else if (value instanceof Map) {
                Map<String, Object> subMap = (Map<String, Object>) value;
                hashMapper(subMap);
            } else if (isCharArray(value)) {
                System.out.println(String.valueOf(value));
            } else {
                throw new IllegalArgumentException(String.valueOf(value));
            }

        }
    }

    public static boolean isNumeric(Object strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble((String) strNum);
        } catch (Exception nfe) {
            return false;
        }
        return true;
    }

    public static boolean isCharArray(Object strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            String s = String.valueOf(strNum);
        } catch (Exception nfe) {
            return false;
        }
        return true;
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
