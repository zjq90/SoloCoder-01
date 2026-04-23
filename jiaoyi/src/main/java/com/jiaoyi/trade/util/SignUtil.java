package com.jiaoyi.trade.util;

import java.util.*;

public class SignUtil {

    public static String getSignContent(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if ("sign".equals(key)) {
                continue;
            }
            Object value = params.get(key);
            if (value != null && !"".equals(value.toString())) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
            }
        }
        
        return sb.toString();
    }

    public static String generateSign(Map<String, Object> params, String privateKey) {
        String signContent = getSignContent(params);
        return RSAUtil.sign(signContent, privateKey);
    }

    public static boolean verifySign(Map<String, Object> params, String sign, String publicKey) {
        String signContent = getSignContent(params);
        return RSAUtil.verify(signContent, sign, publicKey);
    }
}
