package com.jiaoyi.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String uuid;
    private String clientNo;
    private String version;
    private String orderNo;
    private BigDecimal amount;
    private String currency;
    private String notifyUrl;
    private String returnUrl;
    private String subject;
    private String body;
    private String attach;
    private Integer status;
    private String sign;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;
}
