package com.catcoder.demo.annotation.redis;

import java.lang.annotation.*;

/**
 * @ProjectName: demo
 * @Package: com.catcoder.demo.aspect.redis
 * @ClassName: redisMonitor
 * @Author: DELL
 * @Description: redis缓存注解
 * @Date: 2020/6/9 15:52
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCache {
}
