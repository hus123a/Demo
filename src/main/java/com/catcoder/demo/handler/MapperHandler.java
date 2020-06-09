package com.catcoder.demo.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ProjectName: demo
 * @Package: com.catcoder.demo.handler
 * @ClassName: MapperHandler
 * @Author: DELL
 * @Description: mapper类的动态代理类
 * @Date: 2020/6/9 19:32
 * @Version: 1.0
 */
public class MapperHandler<T> implements InvocationHandler {
    /**
     * invocationHandler持有的被代理对象
     */
    T target;

    public MapperHandler() {
    }

    public MapperHandler(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理执行" +method.getName() + "方法");

        if("selectOne".equals(method)){
            System.out.println("匹配");
            for (Object o:
                    args) {
                System.out.println(o.toString());
            }
        }


        Object result = method.invoke(target, args);

        return result;
    }
}
