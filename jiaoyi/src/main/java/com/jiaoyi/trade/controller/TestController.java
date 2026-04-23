package com.jiaoyi.trade.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaoyi.trade.config.DataInitializer;
import com.jiaoyi.trade.entity.Merchant;
import com.jiaoyi.trade.mapper.MerchantMapper;
import com.jiaoyi.trade.util.RSAKeyGenerator;
import com.jiaoyi.trade.util.RSAUtil;
import com.jiaoyi.trade.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private MerchantMapper merchantMapper;

    @GetMapping("/keys")
    public Map<String, Object> getKeys() {
        Map<String, Object> result = new HashMap<>();
        
        Merchant m1 = merchantMapper.selectByClientNo("M001");
        Merchant m2 = merchantMapper.selectByClientNo("M002");
        
        if (m1 != null) {
            result.put("M001_PublicKey", m1.getPublicKey());
            result.put("M001_PrivateKey", m1.getPrivateKey());
        }
        if (m2 != null) {
            result.put("M002_PublicKey", m2.getPublicKey());
            result.put("M002_PrivateKey", m2.getPrivateKey());
        }
        
        if (DataInitializer.merchant1KeyPair != null) {
            result.put("Memory_M001_PublicKey", DataInitializer.merchant1KeyPair.getPublicKey());
            result.put("Memory_M001_PrivateKey", DataInitializer.merchant1KeyPair.getPrivateKey());
        }
        
        return result;
    }

    @PostMapping("/generate-sign")
    public Map<String, Object> generateSign(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String clientNo = (String) request.getOrDefault("client_no", "M001");
            Merchant merchant = merchantMapper.selectByClientNo(clientNo);
            
            if (merchant == null) {
                result.put("error", "商户不存在");
                return result;
            }
            
            Map<String, Object> signParams = new HashMap<>();
            signParams.put("version", request.getOrDefault("version", "1.0"));
            signParams.put("uuid", request.getOrDefault("uuid", IdUtil.simpleUUID()));
            signParams.put("client_no", clientNo);
            signParams.put("order_no", request.getOrDefault("order_no", "ORDER" + System.currentTimeMillis()));
            signParams.put("amount", request.getOrDefault("amount", new BigDecimal("100.00")));
            
            if (request.containsKey("currency")) {
                signParams.put("currency", request.get("currency"));
            }
            if (request.containsKey("subject")) {
                signParams.put("subject", request.get("subject"));
            }
            if (request.containsKey("notify_url")) {
                signParams.put("notify_url", request.get("notify_url"));
            }
            
            String signContent = SignUtil.getSignContent(signParams);
            String sign = RSAUtil.sign(signContent, merchant.getPrivateKey());
            
            signParams.put("sign", sign);
            
            result.put("success", true);
            result.put("sign_content", signContent);
            result.put("sign", sign);
            result.put("full_request", signParams);
            result.put("json", JSON.toJSONString(signParams, true));
            
            boolean verifyResult = RSAUtil.verify(signContent, sign, merchant.getPublicKey());
            result.put("verify_result", verifyResult);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            logger.error("生成签名失败", e);
        }
        
        return result;
    }

    @GetMapping("/sample-request")
    public Map<String, Object> getSampleRequest() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Merchant merchant = merchantMapper.selectByClientNo("M001");
            if (merchant == null) {
                result.put("error", "请先启动项目初始化数据");
                return result;
            }
            
            Map<String, Object> params = new HashMap<>();
            params.put("version", "1.0");
            params.put("uuid", "test-uuid-001");
            params.put("client_no", "M001");
            params.put("order_no", "ORDER" + System.currentTimeMillis());
            params.put("amount", new BigDecimal("100.00"));
            params.put("currency", "CNY");
            params.put("subject", "测试订单");
            params.put("notify_url", "http://localhost:8080/api/notify");
            
            String signContent = SignUtil.getSignContent(params);
            String sign = RSAUtil.sign(signContent, merchant.getPrivateKey());
            params.put("sign", sign);
            
            result.put("说明", "此请求可以直接用于测试 /api/trade/pay 接口");
            result.put("Content-Type", "application/json; charset=UTF-8");
            result.put("请求URL", "POST http://localhost:8080/api/trade/pay");
            result.put("请求JSON", params);
            result.put("格式化JSON", JSON.toJSONString(params, true));
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "运行中");
        result.put("测试接口", new String[]{
            "GET /api/test/keys - 查看商户密钥",
            "GET /api/test/sample-request - 获取示例请求",
            "POST /api/test/generate-sign - 生成签名",
            "POST /api/trade/pay - 交易接口"
        });
        result.put("H2控制台", "http://localhost:8080/h2-console");
        result.put("JDBC URL", "jdbc:h2:mem:testdb");
        result.put("用户名", "sa");
        result.put("密码", "");
        return result;
    }
}
