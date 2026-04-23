package com.jiaoyi.trade.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaoyi.trade.common.ResponseCode;
import com.jiaoyi.trade.dto.TradeRequestDTO;
import com.jiaoyi.trade.dto.TradeResponseDTO;
import com.jiaoyi.trade.entity.*;
import com.jiaoyi.trade.exception.BusinessException;
import com.jiaoyi.trade.mapper.ChannelRequestMapper;
import com.jiaoyi.trade.mapper.TransactionRecordMapper;
import com.jiaoyi.trade.mapper.TransactionRequestMapper;
import com.jiaoyi.trade.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private TransactionRequestMapper transactionRequestMapper;

    @Autowired
    private ChannelRequestMapper channelRequestMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Transactional
    public TradeResponseDTO processTrade(TradeRequestDTO requestDTO) {
        logger.info("处理交易请求: {}", JSON.toJSONString(requestDTO));

        validateRequest(requestDTO);

        Merchant merchant = merchantService.getMerchantByClientNo(requestDTO.getClientNo());
        merchantService.validateMerchantStatus(merchant);

        verifySignature(requestDTO, merchant);

        TransactionRequest existingRequest = transactionRequestMapper.selectByOrderNo(requestDTO.getOrderNo());
        if (existingRequest != null) {
            return buildResponse(existingRequest, merchant);
        }

        TransactionRequest transactionRequest = saveTransactionRequest(requestDTO);

        Channel channel = channelService.selectNextChannel();

        ChannelRequest channelRequest = saveChannelRequest(transactionRequest, channel);

        callChannelAndSaveResponse(channelRequest, transactionRequest, channel);

        TransactionRecord transactionRecord = saveTransactionRecord(transactionRequest, channel, channelRequest);

        return buildSuccessResponse(transactionRecord, merchant, channelRequest);
    }

    private void validateRequest(TradeRequestDTO requestDTO) {
        if (requestDTO.getClientNo() == null || requestDTO.getClientNo().isEmpty()) {
            throw new BusinessException(ResponseCode.PARAM_ERROR, "商户编号不能为空");
        }
        if (requestDTO.getOrderNo() == null || requestDTO.getOrderNo().isEmpty()) {
            throw new BusinessException(ResponseCode.PARAM_ERROR, "订单号不能为空");
        }
        if (requestDTO.getAmount() == null || requestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResponseCode.PARAM_ERROR, "交易金额无效");
        }
        if (requestDTO.getSign() == null || requestDTO.getSign().isEmpty()) {
            throw new BusinessException(ResponseCode.PARAM_ERROR, "签名不能为空");
        }
    }

    private void verifySignature(TradeRequestDTO requestDTO, Merchant merchant) {
        Map<String, Object> params = buildSignParams(requestDTO);
        boolean valid = SignUtil.verifySign(params, requestDTO.getSign(), merchant.getPublicKey());
        
        if (!valid) {
            logger.warn("验签失败，商户: {}, 订单: {}", requestDTO.getClientNo(), requestDTO.getOrderNo());
            throw new BusinessException(ResponseCode.SIGN_ERROR, "签名验证失败");
        }
    }

    private Map<String, Object> buildSignParams(TradeRequestDTO requestDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("version", requestDTO.getVersion());
        params.put("uuid", requestDTO.getUuid());
        params.put("client_no", requestDTO.getClientNo());
        params.put("order_no", requestDTO.getOrderNo());
        params.put("amount", requestDTO.getAmount());
        if (requestDTO.getCurrency() != null) {
            params.put("currency", requestDTO.getCurrency());
        }
        if (requestDTO.getNotifyUrl() != null) {
            params.put("notify_url", requestDTO.getNotifyUrl());
        }
        if (requestDTO.getReturnUrl() != null) {
            params.put("return_url", requestDTO.getReturnUrl());
        }
        if (requestDTO.getSubject() != null) {
            params.put("subject", requestDTO.getSubject());
        }
        if (requestDTO.getBody() != null) {
            params.put("body", requestDTO.getBody());
        }
        if (requestDTO.getAttach() != null) {
            params.put("attach", requestDTO.getAttach());
        }
        return params;
    }

    private TransactionRequest saveTransactionRequest(TradeRequestDTO requestDTO) {
        TransactionRequest request = new TransactionRequest();
        request.setUuid(requestDTO.getUuid() != null ? requestDTO.getUuid() : IdUtil.simpleUUID());
        request.setClientNo(requestDTO.getClientNo());
        request.setVersion(requestDTO.getVersion() != null ? requestDTO.getVersion() : "1.0");
        request.setOrderNo(requestDTO.getOrderNo());
        request.setAmount(requestDTO.getAmount());
        request.setCurrency(requestDTO.getCurrency() != null ? requestDTO.getCurrency() : "CNY");
        request.setNotifyUrl(requestDTO.getNotifyUrl());
        request.setReturnUrl(requestDTO.getReturnUrl());
        request.setSubject(requestDTO.getSubject());
        request.setBody(requestDTO.getBody());
        request.setAttach(requestDTO.getAttach());
        request.setStatus(TransactionRequest.STATUS_PROCESSING);
        request.setSign(requestDTO.getSign());
        
        transactionRequestMapper.insert(request);
        logger.info("保存交易请求成功, id: {}", request.getId());
        return request;
    }

    private ChannelRequest saveChannelRequest(TransactionRequest transactionRequest, Channel channel) {
        ChannelRequest channelRequest = new ChannelRequest();
        channelRequest.setTransactionId(transactionRequest.getId());
        channelRequest.setChannelCode(channel.getChannelCode());
        channelRequest.setChannelOrderNo(IdUtil.simpleUUID());
        channelRequest.setStatus(ChannelRequest.STATUS_REQUESTING);
        
        JSONObject requestData = new JSONObject();
        requestData.put("order_no", transactionRequest.getOrderNo());
        requestData.put("amount", transactionRequest.getAmount());
        requestData.put("currency", transactionRequest.getCurrency());
        requestData.put("client_no", transactionRequest.getClientNo());
        channelRequest.setRequestData(requestData.toJSONString());
        
        channelRequestMapper.insert(channelRequest);
        logger.info("保存通道请求成功, id: {}", channelRequest.getId());
        return channelRequest;
    }

    private void callChannelAndSaveResponse(ChannelRequest channelRequest, 
                                              TransactionRequest transactionRequest, 
                                              Channel channel) {
        logger.info("调用通道: {}, 通道订单号: {}", channel.getChannelName(), channelRequest.getChannelOrderNo());
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        boolean success = Math.random() > 0.1;
        
        JSONObject responseData = new JSONObject();
        if (success) {
            responseData.put("code", "0000");
            responseData.put("msg", "成功");
            responseData.put("channel_order_no", channelRequest.getChannelOrderNo());
            responseData.put("transaction_id", IdUtil.simpleUUID());
            
            channelRequest.setResponseCode("0000");
            channelRequest.setResponseMsg("通道处理成功");
            channelRequest.setStatus(ChannelRequest.STATUS_SUCCESS);
            
            transactionRequest.setStatus(TransactionRequest.STATUS_SUCCESS);
        } else {
            responseData.put("code", "2001");
            responseData.put("msg", "通道处理失败");
            
            channelRequest.setResponseCode("2001");
            channelRequest.setResponseMsg("通道处理失败");
            channelRequest.setStatus(ChannelRequest.STATUS_FAILED);
            
            transactionRequest.setStatus(TransactionRequest.STATUS_FAILED);
        }
        
        channelRequest.setResponseData(responseData.toJSONString());
        channelRequestMapper.updateById(channelRequest);
        transactionRequestMapper.updateById(transactionRequest);
        
        logger.info("通道响应: 订单号={}, 状态={}", channelRequest.getChannelOrderNo(), channelRequest.getStatus());
    }

    private TransactionRecord saveTransactionRecord(TransactionRequest transactionRequest, 
                                                      Channel channel, 
                                                      ChannelRequest channelRequest) {
        TransactionRecord record = new TransactionRecord();
        record.setUuid(transactionRequest.getUuid());
        record.setClientNo(transactionRequest.getClientNo());
        record.setOrderNo(transactionRequest.getOrderNo());
        record.setChannelCode(channel.getChannelCode());
        record.setChannelOrderNo(channelRequest.getChannelOrderNo());
        record.setAmount(transactionRequest.getAmount());
        record.setCurrency(transactionRequest.getCurrency());
        
        if (channelRequest.getStatus() == ChannelRequest.STATUS_SUCCESS) {
            record.setStatus(TransactionRecord.STATUS_SUCCESS);
        } else {
            record.setStatus(TransactionRecord.STATUS_FAILED);
        }
        
        record.setNotifyUrl(transactionRequest.getNotifyUrl());
        record.setSubject(transactionRequest.getSubject());
        
        transactionRecordMapper.insert(record);
        logger.info("保存交易记录成功, id: {}", record.getId());
        return record;
    }

    private TradeResponseDTO buildResponse(TransactionRequest existingRequest, Merchant merchant) {
        TransactionRecord record = transactionRecordMapper.selectByOrderNo(existingRequest.getOrderNo());
        if (record != null) {
            TradeResponseDTO response = new TradeResponseDTO();
            response.setClientNo(record.getClientNo());
            response.setCode(ResponseCode.SUCCESS);
            response.setMsg("订单已存在");
            response.setOrderNo(record.getOrderNo());
            response.setChannelCode(record.getChannelCode());
            response.setChannelOrderNo(record.getChannelOrderNo());
            response.setAmount(record.getAmount());
            response.setStatus(record.getStatus());
            
            Map<String, Object> signParams = new HashMap<>();
            signParams.put("client_no", response.getClientNo());
            signParams.put("code", response.getCode());
            signParams.put("order_no", response.getOrderNo());
            if (response.getChannelCode() != null) {
                signParams.put("channel_code", response.getChannelCode());
            }
            signParams.put("status", response.getStatus());
            
            String sign = SignUtil.generateSign(signParams, merchant.getPrivateKey());
            response.setSign(sign);
            
            return response;
        }
        
        TradeResponseDTO response = new TradeResponseDTO();
        response.setClientNo(existingRequest.getClientNo());
        response.setCode("1005");
        response.setMsg("订单已存在，处理中");
        response.setOrderNo(existingRequest.getOrderNo());
        response.setStatus(existingRequest.getStatus());
        
        return response;
    }

    private TradeResponseDTO buildSuccessResponse(TransactionRecord record, Merchant merchant, ChannelRequest channelRequest) {
        TradeResponseDTO response = new TradeResponseDTO();
        response.setClientNo(record.getClientNo());
        
        if (record.getStatus() == TransactionRecord.STATUS_SUCCESS) {
            response.setCode(ResponseCode.SUCCESS);
            response.setMsg("交易成功");
        } else {
            response.setCode(ResponseCode.CHANNEL_REQUEST_FAILED);
            response.setMsg("交易失败");
        }
        
        response.setOrderNo(record.getOrderNo());
        response.setChannelCode(record.getChannelCode());
        response.setChannelOrderNo(record.getChannelOrderNo());
        response.setAmount(record.getAmount());
        response.setStatus(record.getStatus());
        
        Map<String, Object> signParams = new HashMap<>();
        signParams.put("client_no", response.getClientNo());
        signParams.put("code", response.getCode());
        signParams.put("order_no", response.getOrderNo());
        signParams.put("channel_code", response.getChannelCode());
        signParams.put("channel_order_no", response.getChannelOrderNo());
        signParams.put("amount", response.getAmount());
        signParams.put("status", response.getStatus());
        
        String sign = SignUtil.generateSign(signParams, merchant.getPrivateKey());
        response.setSign(sign);
        
        return response;
    }
}
