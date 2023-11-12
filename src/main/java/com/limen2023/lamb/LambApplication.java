package com.limen2023.lamb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.limen2023.lamb.**.dao") //加载Mapper/dao接口
@SpringBootApplication
public class LambApplication {
    public static void main(String[] args) {
        SpringApplication.run(LambApplication.class,args);
    }
}
