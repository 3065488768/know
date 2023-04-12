package com.example.know.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 检测redis是否连接
 *
 * @author bookWorm
 */
public class RedisConnectUtil {
    public static boolean redisIsConnect() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        try {
            jedis.ping();
        } catch (JedisConnectionException e) {
            return false;
        }
        return true;
    }
}
