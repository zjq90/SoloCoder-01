package com.jiaoyi.trade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TradeRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("version")
    private String version;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("client_no")
    private String clientNo;

    @JsonProperty("order_no")
    private String orderNo;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("notify_url")
    private String notifyUrl;

    @JsonProperty("return_url")
    private String returnUrl;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("attach")
    private String attach;

    @JsonProperty("sign")
    private String sign;
}
