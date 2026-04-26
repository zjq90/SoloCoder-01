package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int PROFIT_STATUS_PENDING = 0;
    public static final int PROFIT_STATUS_SUCCESS = 1;
    public static final int PROFIT_STATUS_FAILED = 2;

    private Long id;

    private String orderNo;

    private String agentCode;

    private String agentName;

    private BigDecimal transactionAmount;

    private Integer orderStatus;

    private Integer profitStatus;

    private LocalDateTime transactionTime;

    private LocalDateTime createTime;
}
