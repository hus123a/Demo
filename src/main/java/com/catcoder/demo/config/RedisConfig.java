package com.catcoder.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: catCoder-parent
 * @description: reids配置类
 * @author: CodeCat
 * @create: 2020-06-12 23:21
 **/
@Configuration
@Slf4j
@RefreshScope
public class RedisConfig {
    Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    /**
     * redis:
     *     # redis数据库索引（默认为0），我们使用索引为3的数据库，避免和其他数据库冲突
     *     database: 0
     *     # redis服务器地址（默认为localhost）
     *     host: 39.99.166.88
     *     # redis端口（默认为6379）
     *     port: 6379
     *     # redis访问密码（默认为空）
     *     password:
     *     # redis连接超时时间（单位为毫秒）
     *     timeout: 1000
     *     # redis连接池配置
     *     jedis:
     *       pool:
     *         # 最大可用连接数（默认为8，负数表示无限）
     *         max-active: -1
     *         # 最大空闲连接数（默认为8，负数表示无限）
     *         max-idle: 8
     *         # 最小空闲连接数（默认为0，该值只有为正数才有作用）
     *         min-idle: 0
     *         # 从连接池中获取连接最大等待时间（默认为-1，单位为毫秒，负数表示无限）
     *         max-wait: -1
     */


   /* @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.timeout}")
    private int timeout;

    @Value("${redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${redis.password}")
    private String password;*/



    @Bean
    public JedisPool redisPoolFactory()  throws Exception{


        log.info("JedisPool注入成功！！");

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(-1);
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxWaitMillis(100000);
        jedisPoolConfig.setTestOnCreate(false);
//        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(false);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "39.99.166.88", 6379, 100000);
        return jedisPool;
    }
}

