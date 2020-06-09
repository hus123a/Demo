package com.catcoder.demo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author DELL
 */
@Component
@DependsOn(value = {"redisTemplate"})
@RefreshScope
public class RedisUtil {

    private static Logger logger =  LoggerFactory.getLogger(RedisUtil.class);
    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${spring.redis.open: #{false}}")
    private static boolean open;

    public RedisUtil() {
        super();
        logger.info("RedisUtil构建完成！");
    }

    @Autowired
    private static RedisTemplate redisTemplate;

    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 7200;

    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    public final static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping()
            .create();

    public static boolean exists(String key) {
        if (!open) {
            logger.info("redis已经关闭");
            return false;
        }

        return redisTemplate.hasKey(key);
    }

    public  static void set(String key, Object value, long expire) {
        if (!open) {
            logger.info("redis已经关闭");
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, toJson(value));

        } catch (Exception e0) {
            System.out.println(e0.getMessage());
        }
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public static void set(String key, Object value) {
        if (!open) {
            logger.info("redis已经关闭");
            return;
        }
        try {

            redisTemplate.opsForValue().set(key, toJson(value));

        } catch (Exception e0) {
            System.out.println(e0.getMessage());
        }
    }

    public static <T> T get(String key, Class<T> clazz, long expire) {
        if (!open) {
            logger.info("redis已经关闭");
            return null;
        }

        String value = (String) redisTemplate.opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public static <T> T get(String key, Class<T> clazz) {
        if (!open) {
            logger.info("redis已经关闭");
            return null;
        }

        return get(key, clazz, NOT_EXPIRE);
    }

    public static String get(String key, long expire) {
        if (!open) {
            logger.info("redis已经关闭");
            return null;
        }

        String value =(String) redisTemplate.opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public static String get(String key) {
        if (!open) {
            logger.info("redis已经关闭");
            return null;
        }

        return get(key, NOT_EXPIRE);
    }

    public static void delete(String key) {
        if (!open) {
            logger.info("redis已经关闭");
            return;
        }

        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public static void delete(String... keys) {
        if (!open) {
            logger.info("redis已经关闭");
            return;
        }

        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }

    public static void deletePattern(String pattern) {
        if (!open) {
            logger.info("redis已经关闭");
            return;
        }

        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * Object转成JSON数据
     */
    private static String toJson(Object object) {
        if (!open) {
            logger.info("redis已经关闭");
            return null;
        }

        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }

        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private static <T> T fromJson(String json, Class<T> clazz) {
        if (!open) {
            logger.info("redis已经关闭");
            return null;
        }
        return gson.fromJson(json, clazz);
    }

}
