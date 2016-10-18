package com.bmosdev.ratemyphoto.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lime.util.Strings;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHelper {

    private static final String DOTS = "...";

    private static final ObjectMapper strictJsonMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);

    private static final ObjectMapper liberalJsonMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

    public static <T> List<T> deserializeJsonList(String serializedObject, Class<T> clazz, boolean useStrictMapper) throws IOException {
        ObjectMapper jsonMapper = selectJsonMapper(useStrictMapper);
        return jsonMapper.readValue(serializedObject, new TypeReference<List<T>>() {});
    }

    public static <T> T deserializeJson(String serializedObject, Class<T> clazz, boolean useStrictMapper) throws IOException {
        ObjectMapper jsonMapper = selectJsonMapper(useStrictMapper);
        return jsonMapper.readValue(serializedObject, clazz);
    }

    public static <T> T deserializeJson(byte[] serializedObject, Class<T> clazz, boolean useStrictMapper) throws IOException {
        ObjectMapper jsonMapper = selectJsonMapper(useStrictMapper);
        return jsonMapper.readValue(serializedObject, clazz);
    }

    public static <T> T deserializeJson(URL url, Class<T> clazz, boolean useStrictMapper) throws IOException {
        ObjectMapper jsonMapper = selectJsonMapper(useStrictMapper);
        return jsonMapper.readValue(url, clazz);
    }

    public static String serializeJson(Object object) throws IOException {
        return selectJsonMapper(true).writeValueAsString(object);
    }

    public static String serializeJsonSilently(Object object) {
        try {
            return serializeJson(object);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception: " + e);
        }
    }

    private static ObjectMapper selectJsonMapper(boolean useStrictMapper) {
        return useStrictMapper ? strictJsonMapper : liberalJsonMapper;
    }

    public static String toBriefString(String serializedJson, int valueMaxLength, String... briefableFields) {
        if (Strings.isBlank(serializedJson) || briefableFields.length == 0 || valueMaxLength < DOTS.length()) {
            return serializedJson;
        }
        StringBuilder pattern = new StringBuilder();
        pattern.append("((?:");
        for (int i = 0; i < briefableFields.length; i++) {
            if (i > 0) {
                pattern.append("|");
            }
            pattern.append("(?:\"?").append(briefableFields[i]).append("\"?)");
        }
        pattern.append(")\\s*:\\s*\")(.{").append(valueMaxLength + 1);
        pattern.append(",}?)((?<!\\\\)\")");
        Matcher matcher = Pattern.compile(pattern.toString()).matcher(serializedJson);

        StringBuffer result = new StringBuffer();
        while(matcher.find()){
            String value = matcher.group(2);
            String replacement = matcher.group(1) + StringUtil.shortenString(value, valueMaxLength) + matcher.group(3);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static class BriefSerializedJson {

        private final String serializedJson;
        private final int valueMaxLength;
        private final String[] briefableFields;

        public BriefSerializedJson(String serializedJson, int valueMaxLength, String... briefableFields) {
            this.serializedJson = serializedJson;
            this.valueMaxLength = valueMaxLength;
            this.briefableFields = briefableFields;
        }

        @Override
        public String toString() {
            return toBriefString(this.serializedJson, valueMaxLength, briefableFields);
        }
    }
}
