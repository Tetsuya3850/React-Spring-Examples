package com.example.instagramserver.commons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;
import java.util.Map;

public class TestUtils {

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map decodeJWTPayload(String token){
        String base64EncodedBody = token.split("\\.")[1];
        Base64 base64Url = new Base64(true);
        String payload = new String(base64Url.decode(base64EncodedBody));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonPayload;
        try {
            jsonPayload = mapper.readValue(payload,
                    new TypeReference<Map<String, String>>() {
                    });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return jsonPayload;
    }

    public static String dummyStringWithSpecifiedLength(int length){
        char[] chars = new char[length];
        Arrays.fill(chars, '*');
        return new String(chars);
    }

    public static void threadSleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
