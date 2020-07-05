package com.catCoder.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "server1")
public interface TestApi {

    @GetMapping("sayHello")
    public String sayHello();
}
