package com.jiaoyi.trade.controller;

import com.alibaba.fastjson.JSON;
import com.jiaoyi.trade.common.ResponseCode;
import com.jiaoyi.trade.common.Result;
import com.jiaoyi.trade.dto.TradeRequestDTO;
import com.jiaoyi.trade.dto.TradeResponseDTO;
import com.jiaoyi.trade.exception.BusinessException;
import com.jiaoyi.trade.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<TradeResponseDTO> pay(@RequestBody TradeRequestDTO requestDTO) {
        logger.info("收到交易请求: {}", JSON.toJSONString(requestDTO));
        try {
            TradeResponseDTO response = tradeService.processTrade(requestDTO);
            logger.info("交易响应: {}", JSON.toJSONString(response));
            return Result.success(response);
        } catch (BusinessException e) {
            logger.error("业务异常: code={}, msg={}", e.getCode(), e.getMsg());
            return Result.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("系统异常", e);
            return Result.error(ResponseCode.SYSTEM_ERROR, "系统异常");
        }
    }

    @PostMapping(value = "/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<TradeResponseDTO> query(@RequestBody TradeRequestDTO requestDTO) {
        logger.info("收到查询请求: {}", JSON.toJSONString(requestDTO));
        return Result.success();
    }
}
