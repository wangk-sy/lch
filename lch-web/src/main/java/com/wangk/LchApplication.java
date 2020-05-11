package com.wangk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName :com.wangk.LchApplication
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/6 23:11
 * @Version :1.0
 **/
@SpringBootApplication
@MapperScan("com.wangk.mapper")
public class LchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LchApplication.class,args);
    }
}
