package com.catCoder.watcherBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DealInstanceFactory {

    private DealInstanceFactory(){}

    private static ConcurrentHashMap<String, Class> dealInstanceMap = new ConcurrentHashMap<>();


    static {
        dealInstanceMap.put("one", OneDealMethod.class);
        dealInstanceMap.put("two", TwoDealMethod.class);
        dealInstanceMap.put("three", ThreeDealMethod.class);
    }
    @Autowired
    private ApplicationContext applicationContext;


    public DealMethod getDealMethod(String instanceKey){
        return (DealMethod) applicationContext.getBean(dealInstanceMap.get(instanceKey));
    }

}
