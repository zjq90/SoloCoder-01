package com.qianzhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qianzhi.mapper")
public class QianzhiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QianzhiApplication.class, args);
    }

}
