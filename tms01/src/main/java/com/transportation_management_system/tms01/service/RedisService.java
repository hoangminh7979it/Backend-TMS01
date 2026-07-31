package com.transportation_management_system.tms01.service;

public interface RedisService {

    void save(String key, Object value, long timeoutInSeconds);

    Object get(String key);

    void delete(String key);

    boolean hasKey(String key);

    void blacklistToken(String token, long timeoutInMs);

    boolean isTokenBlacklisted(String token);
}
