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
@RefreshScope
public class RedisUtil {

    private static Logger logger =  LoggerFactory.getLogger(RedisUtil.class);


    private static Integer MAX_WAIT_TIME = 1000;

    private static Integer KEY_EXIST_TIME = 500;
    private static Integer BUFFER_TIME = 10;

    private static final String LOCK_KEY = "LOCK_KEY";

    private static final String LOCK_SUCCESS = "OK";
    private static final String REMOVE_SUCCESS = "1";



    private static final String REMOVE_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                                                "   return redis.call('del',KEYS[1]) " +
                                                "else" +
                                                "   return 0 " +
                                                "end";

    public RedisUtil() {
        super();
        logger.info("RedisUtil构建完成！");
    }

//    @Autowired
//    private static RedisTemplate redisTemplate;

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



    /**
     * 分布式获取锁
     * @param owner
     * @return
     */
    public static boolean setNxKey(String owner, JedisPool jedisPool){
        Jedis jedis = jedisPool.getResource();
        boolean flag = false;
        try{
            Long startTryLockTime = System.currentTimeMillis();
            for(;;){
                Long costTime = System.currentTimeMillis() - startTryLockTime;
                //判断当前获取锁的时间是否超时
                if(costTime > MAX_WAIT_TIME) {
                    logger.error(Thread.currentThread().getName()+"获取锁超时超时");
                    break;
                }

                //尝试获取redis锁
                SetParams params = SetParams.setParams().nx().px(KEY_EXIST_TIME);

                String lockStatu = jedis.set(LOCK_KEY, owner, params);
                if(StringUtils.equals(LOCK_SUCCESS, lockStatu)){
                    logger.info(Thread.currentThread().getName()+"获取锁成功");
                    flag = true;
                    break;
                }
            }
            return flag;
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
            logger.info(Thread.currentThread().getName()+"开始释放锁");
            Object evalResult = jedis.eval(REMOVE_SCRIPT,
                    Collections.singletonList(LOCK_KEY),
                    Collections.singletonList(owner));

            return StringUtils.equals(REMOVE_SUCCESS, evalResult.toString());
        }finally {
            jedis.close();
        }
    }





}
