package com.fenzhang;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@MapperScan("com.fenzhang.mapper")
@EnableScheduling
public class FenzhangApplication {

    private static final Logger logger = LoggerFactory.getLogger(FenzhangApplication.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        logger.info("========== 分账系统重构版启动中 ==========");
        logger.info("启动时间: {}", LocalDateTime.now().format(FORMATTER));
        logger.info("核心特性: 分润计算使用数据库存储过程");
        
        SpringApplication.run(FenzhangApplication.class, args);
        
        logger.info("========== 分账系统重构版启动成功 ==========");
        logger.info("访问地址: http://localhost:8080");
    }

    @PostConstruct
    public void init() {
        logger.info("系统初始化完成，核心组件已就绪");
        logger.info("分润计算模式: 数据库存储过程模式");
    }
}
