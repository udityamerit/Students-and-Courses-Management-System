package edu.ccrm.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleJsonUtils {

    /**
     * Parses a flat JSON object string into a Map of key-value pairs.
     * This is a very basic parser meant only for simple, non-nested JSON payloads.
     */
    public static Map<String, String> parseFlatJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) return map;
        
        String cleanJson = json.trim();
        if (cleanJson.startsWith("{") && cleanJson.endsWith("}")) {
            cleanJson = cleanJson.substring(1, cleanJson.length() - 1).trim();
        }

        // Extremely simplified parsing, assumes no commas inside values
        String[] pairs = cleanJson.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim();
                String value = kv[1].trim();
                
                // Remove quotes
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * Escapes a string for inclusion in a JSON value.
     */
    public static String escapeJson(String text) {
        if (text == null) return "null";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
