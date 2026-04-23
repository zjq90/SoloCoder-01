package com.jiaoyi.trade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TradeResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("client_no")
    private String clientNo;

    @JsonProperty("code")
    private String code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("order_no")
    private String orderNo;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("channel_code")
    private String channelCode;

    @JsonProperty("channel_order_no")
    private String channelOrderNo;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("sign")
    private String sign;
}
