package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Stores user's token infos (session, cursor) in redis.
 */
@Repository
public class UserTokenRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setValue(final String hashKey, final String key, final String value) {
        redisTemplate.boundHashOps(hashKey).put(key, value);
    }

    public String getValue(final String hashKey, final String key) {
        return (String) redisTemplate.boundHashOps(hashKey).get(key);
    }
}