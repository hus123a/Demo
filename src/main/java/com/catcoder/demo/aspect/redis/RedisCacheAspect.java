package com.catcoder.demo.aspect.redis;

import com.catcoder.demo.utils.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: demo
 * @Package: com.catcoder.demo.aspect.redis
 * @ClassName: RedisCacheAspect
 * @Author: DELL
 * @Description: redis缓存注解切面
 * @Date: 2020/6/9 16:11
 * @Version: 1.0
 */
@Component
@Aspect
public class RedisCacheAspect {

    private static Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

    /**
     * 切注解
     */
    @Pointcut("@annotation(com.catcoder.demo.annotation.redis.RedisCache)")
    public void redisCache(){}

    @Around("redisCache()")
    public Object Interceptor(ProceedingJoinPoint joinPoint){

        logger.info("进入缓存切面");
        long startTime = System.currentTimeMillis();
        String applId = null;
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            applId = String.valueOf(args[0]);
        }
        //获取目标方法所在类
        String target = joinPoint.getTarget().toString();
        String className = target.split("@")[0];

        //获取目标方法的方法名称
        String methodName = joinPoint.getSignature().getName();

        //redis中key格式：    applId:方法名称
        String redisKey = applId + ":" + className + "." + methodName;

        Object obj= RedisUtil.get(redisKey);

        if(obj!=null){
            logger.info("**********从Redis中查到了数据**********");
            logger.info("Redis的KEY值:"+redisKey);
            logger.info("REDIS的VALUE值:"+obj.toString());
            return obj;
        }
        long endTime = System.currentTimeMillis();
        logger.info("Redis缓存AOP处理所用时间:"+(endTime-startTime));
        logger.info("**********没有从Redis查到数据**********");
        try{
            obj = joinPoint.proceed();
        }catch(Throwable e){
            e.printStackTrace();
        }
        logger.info("**********开始从MySQL查询数据**********");
        //后置：将数据库查到的数据保存到Redis
        try{
            RedisUtil.set(redisKey,obj);
            logger.info("**********数据成功保存到Redis缓存!!!**********");
            logger.info("Redis的KEY值:"+redisKey);
            logger.info("REDIS的VALUE值:"+obj.toString());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return obj;
    }
}
