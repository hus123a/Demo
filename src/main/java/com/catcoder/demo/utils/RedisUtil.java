package com.catcoder.demo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
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

    private static Integer MAX_WAIT_TIME = 100000;

    private static Integer KEY_EXIST_TIME = 500;
    private static Integer BUFFER_TIME = 10;

    private static final String LOCK_KEY = "LOCK_KEY";

    private static final String LOCK_SUCCESS = "OK";
    private static final String REMOVE_SUCCESS = "1";

    //SET命令的参数
    private static SetParams params = SetParams.setParams().nx().px(KEY_EXIST_TIME);

    private static final String REMOVE_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                                                "   return redis.call('del',KEYS[1]) " +
                                                "else" +
                                                "   return 0 " +
                                                "end";

    public RedisUtil() {
        super();
        logger.info("RedisUtil构建完成！");
    }

    @Autowired
    private static RedisTemplate redisTemplate;

//    @Autowired
//    private static JedisPool jedisPool;




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
     * 分布式获取锁
     * @param owner
     * @return
     */
    public static boolean setNxKey(String owner, JedisPool jedisPool){
        Jedis jedis = jedisPool.getResource();
        try{
            Long startTryLockTime = System.currentTimeMillis();
            for(;;){
                Long costTime = System.currentTimeMillis() - startTryLockTime;
                //判断当前获取锁的时间是否超时
                if(costTime > MAX_WAIT_TIME) {
                    logger.error(Thread.currentThread().getName()+"获取锁超时超时");
                    return false;
                }

                //尝试获取redis锁
                String lockStatu = jedis.set(LOCK_KEY, owner, params);
                logger.error(Thread.currentThread().getName()+"获取lockStatu：" + lockStatu);
                if(StringUtils.equals(LOCK_KEY, lockStatu)){
                    logger.error(Thread.currentThread().getName()+"获取锁成功");
                    return true;
                }
            }
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public static boolean removeNxKey(String owner,JedisPool jedisPool){
        Jedis jedis  = jedisPool.getResource();
        try{
            //尝试执行脚本删除获取的锁 key
            logger.error(Thread.currentThread().getName()+"开始释放锁");
            Object evalResult = jedis.eval(REMOVE_SCRIPT,
                    Collections.singletonList(LOCK_KEY),
                    Collections.singletonList(owner));
            logger.error(Thread.currentThread().getName()+"获得的result:" + evalResult);
            return StringUtils.equals(REMOVE_SUCCESS, evalResult.toString());
        }finally {
            jedis.close();
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
