package com.catcoder.demo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: demo
 * @description: mybatisPlus配置类
 * @author: CodeCat
 * @create: 2020-05-07 23:35
 **/
@EnableTransactionManagement
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

