package com.yjxxt.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication    //设定其为启动类   组合注解
@MapperScan("com.yjxxt.crm.mapper")
public class Starter {
    public static void main(String[] args){
        SpringApplication.run(Starter.class,args);
    }
}
