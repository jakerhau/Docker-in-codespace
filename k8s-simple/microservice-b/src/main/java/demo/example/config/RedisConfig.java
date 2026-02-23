package demo.example.config;

import redis.clients.jedis.Jedis;

public class RedisConfig {
    private static final String REDIS_HOST = "redis";
    private static final int REDIS_PORT = 6379;

    public Jedis createJedisClient() {
        return new Jedis(REDIS_HOST, REDIS_PORT);
    }
}