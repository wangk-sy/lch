package com.wangk.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName :MybatisConfig
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/11 16:36
 * @Version :1.0
 **/
@EnableTransactionManagement
@Configuration
public class MybatisConfig  {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

}
