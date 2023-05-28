package com.majiang.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@MapperScan("com.majiang.community.mapper")
@EnableAsync
public class MajiangApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajiangApplication.class, args);
    }

}
