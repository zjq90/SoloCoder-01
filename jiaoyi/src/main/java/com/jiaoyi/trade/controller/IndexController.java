package com.jiaoyi.trade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index() {
        return "交易系统API服务已启动！\n" +
               "接口地址: /api/trade/pay (POST)\n" +
               "H2控制台: /h2-console\n" +
               "Content-Type: application/json; charset=UTF-8";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
