package com.jiaoyi.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jiaoyi.trade.mapper")
public class TradeSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeSystemApplication.class, args);
    }
}
