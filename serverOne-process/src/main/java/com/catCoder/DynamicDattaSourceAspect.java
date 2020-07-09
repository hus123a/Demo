package com.catCoder;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
//保证在@Transactional之前执行
@Order(-1)
@Component
@Slf4j
public class DynamicDattaSourceAspect {

    //改变数据源
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String dbId = request.getHeader("dbId");

        if (!DynamicDataSourceContextHolder.isContainsDataSource(dbId)) {
            //joinPoint.getSignature() ：获取连接点的方法签名对象
            log.error("数据源 " + dbId + " 不存在");
        } else {
            log.debug("使用数据源：" + dbId);
            DynamicDataSourceContextHolder.setDataSourceType(dbId);
        }
    }

    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String dbId = request.getHeader("dbId");
        log.debug("清除数据源 " + dbId + " !");
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
