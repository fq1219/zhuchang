package com.zhuchang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy=true)
@SpringBootApplication
@EnableCaching
public class ZhuchangApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhuchangApplication.class, args);
    }
}
