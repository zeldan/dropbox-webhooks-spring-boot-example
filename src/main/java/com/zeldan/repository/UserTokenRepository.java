package com.zeldan.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Stores user's token info (session, cursor) in redis.
 */
@Repository
public class UserTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public UserTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(String hashKey, String key, String value) {
        redisTemplate.boundHashOps(hashKey).put(key, value);
    }

    public String getValue(String hashKey, String key) {
        return (String) redisTemplate.boundHashOps(hashKey).get(key);
    }
}