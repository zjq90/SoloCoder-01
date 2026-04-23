package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String statementNo;

    private String statementPeriod;

    private String agentCode;

    private String agentName;

    private BigDecimal totalTransactionAmount;

    private BigDecimal totalProfitAmount;

    private Integer transactionCount;

    private Integer profitCount;

    private Integer statementStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
