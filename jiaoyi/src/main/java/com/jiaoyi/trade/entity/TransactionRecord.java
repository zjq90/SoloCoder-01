package com.jiaoyi.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String uuid;
    private String clientNo;
    private String orderNo;
    private String channelCode;
    private String channelOrderNo;
    private BigDecimal amount;
    private String currency;
    private Integer status;
    private String notifyUrl;
    private String subject;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;
}
