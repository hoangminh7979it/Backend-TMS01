package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "BLACKLIST_TOKEN:";

    @Override
    public void save(String key, Object value, long timeoutInSeconds) {
        try {
            redisTemplate.opsForValue().set(key, String.valueOf(value), timeoutInSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Lỗi khi ghi dữ liệu vào Redis với key={}: {}", key, e.getMessage());
        }
    }

    @Override
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Lỗi khi đọc dữ liệu từ Redis với key={}: {}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Lỗi khi xóa key khỏi Redis key={}: {}", key, e.getMessage());
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Lỗi khi kiểm tra key tồn tại trên Redis key={}: {}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public void blacklistToken(String token, long timeoutInMs) {
        if (token == null || token.isBlank()) return;
        String key = BLACKLIST_PREFIX + token;
        try {
            redisTemplate.opsForValue().set(key, "LOGGED_OUT", timeoutInMs, TimeUnit.MILLISECONDS);
            log.info("Đã đưa Token vào danh sách đen (Blacklist) trên Redis thành công.");
        } catch (Exception e) {
            log.error("Lỗi khi blacklist token trên Redis: {}", e.getMessage());
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isBlank()) return false;
        String key = BLACKLIST_PREFIX + token;
        return hasKey(key);
    }
}
