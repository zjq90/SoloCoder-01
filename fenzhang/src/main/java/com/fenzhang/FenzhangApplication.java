package com.fenzhang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.fenzhang.mapper")
@EnableScheduling
public class FenzhangApplication {

    public static void main(String[] args) {
        SpringApplication.run(FenzhangApplication.class, args);
        System.out.println("========== 分账系统启动成功 ==========");
    }
}
