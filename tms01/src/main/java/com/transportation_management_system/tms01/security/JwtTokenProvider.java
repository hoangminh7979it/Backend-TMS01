package com.transportation_management_system.tms01.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:tms01SecretKeyForJwtTokenGenerationSecurity6879AdminVeryLongKeyNeededForHmacSha256}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}") // 24 hours default
    private long jwtExpirationMs;

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        long exp = now + jwtExpirationMs;

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", username, now / 1000, exp / 1000);

        String encodedHeader = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

        String signatureInput = encodedHeader + "." + encodedPayload;
        String signature = hmacSha256(signatureInput, jwtSecret);

        return signatureInput + "." + signature;
    }

    public String getUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            // Extract "sub":"username"
            int subIdx = payloadJson.indexOf("\"sub\":\"");
            if (subIdx == -1) return null;
            int start = subIdx + 7;
            int end = payloadJson.indexOf("\"", start);
            return payloadJson.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signatureInput = parts[0] + "." + parts[1];
            String expectedSignature = hmacSha256(signatureInput, jwtSecret);
            if (!expectedSignature.equals(parts[2])) return false;

            // Check expiration
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            int expIdx = payloadJson.indexOf("\"exp\":");
            if (expIdx != -1) {
                int start = expIdx + 6;
                int end = payloadJson.indexOf("}", start);
                if (end == -1) end = payloadJson.indexOf(",", start);
                long exp = Long.parseLong(payloadJson.substring(start, end).trim());
                if (System.currentTimeMillis() / 1000 > exp) {
                    return false; // Expired
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(hash);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi sinh chữ ký JWT HMAC-SHA256", e);
        }
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
