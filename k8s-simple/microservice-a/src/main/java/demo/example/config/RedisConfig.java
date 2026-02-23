package demo.example.config;

import java.util.logging.Logger;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Component(service = RedisConfig.class)
public class RedisConfig {

    private JedisPool jedisPool;

    private static final Logger logger = Logger.getLogger(RedisConfig.class.getName());

    @Activate
    public void activate() {
        jedisPool = new JedisPool("redis", 6379);

        try (Jedis jedis = jedisPool.getResource()) {
            String response = jedis.ping();
            if ("PONG".equals(response)) {
                logger.info("Successfully connected to Redis");
            } else {
                logger.warning(() -> "Unexpected response from Redis: " + response);
            }
        } catch (Exception ex) {
            logger.severe(() -> "Failed to connect to Redis: " + ex.getMessage());
            jedisPool = null;
           
        }
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }
}