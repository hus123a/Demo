package com.catCoder.handler;

import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ProjectName: demo
 * @Package: com.catcoder.demo.handler
 * @ClassName: ProxyFactory
 * @Author: DELL
 * @Description: 代理类的工厂类
 * @Date: 2020/6/9 19:52
 * @Version: 1.0
 */
@Component
public class ProxyFactory {

    private static Map<Class, Object> proxyContent = new ConcurrentHashMap<>();

    public static  <T> T getProxyInstance(T target,Class clazz){
        if(!proxyContent.containsKey(clazz)){
           synchronized (ProxyFactory.class){
               if(!proxyContent.containsKey(clazz)){
                   System.out.println("初始化proxy");
                   proxyContent.put(clazz,
                           Proxy.newProxyInstance(
                                   clazz.getClassLoader(),
                                   new Class<?>[]{clazz},
                                   new MapperHandler<T>(target)));
               }
           }
        }else {
            System.out.println("获取已经存在的proxy");
        }

        return (T) proxyContent.get(clazz);
    }

}
