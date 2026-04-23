package com.qianzhi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.qianzhi.mapper")
public class MybatisConfig {
}
