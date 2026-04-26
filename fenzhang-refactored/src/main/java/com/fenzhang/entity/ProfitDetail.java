package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProfitDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String profitNo;

    private String orderNo;

    private String agentCode;

    private String agentName;

    private String parentAgentCode;

    private BigDecimal transactionAmount;

    private BigDecimal platformProfitRate;

    private BigDecimal agentProfitRate;

    private BigDecimal profitRateDiff;

    private BigDecimal profitAmount;

    private LocalDateTime profitTime;

    private LocalDateTime createTime;
}
