package com.jiaoyi.trade.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TestSignUtil {

    public static void main(String[] args) throws Exception {
        RSAKeyGenerator.KeyPair keyPair = RSAKeyGenerator.generateKeyPair();
        
        System.out.println("===== 测试RSA密钥对 =====");
        System.out.println("公钥: " + keyPair.getPublicKey());
        System.out.println();
        System.out.println("私钥: " + keyPair.getPrivateKey());
        System.out.println();
        
        Map<String, Object> params = new HashMap<>();
        params.put("version", "1.0");
        params.put("uuid", "test-001");
        params.put("client_no", "M001");
        params.put("order_no", "ORDER20240101001");
        params.put("amount", new BigDecimal("100.00"));
        params.put("currency", "CNY");
        params.put("subject", "测试订单");
        
        String signContent = SignUtil.getSignContent(params);
        System.out.println("===== 待签名字符串 =====");
        System.out.println(signContent);
        System.out.println();
        
        String sign = RSAUtil.sign(signContent, keyPair.getPrivateKey());
        System.out.println("===== 签名结果 =====");
        System.out.println(sign);
        System.out.println();
        
        boolean verifyResult = RSAUtil.verify(signContent, sign, keyPair.getPublicKey());
        System.out.println("===== 验签结果 =====");
        System.out.println(verifyResult ? "验签成功" : "验签失败");
        System.out.println();
        
        System.out.println("===== 完整测试请求JSON =====");
        params.put("sign", sign);
        System.out.println(com.alibaba.fastjson.JSON.toJSONString(params, true));
    }
}
