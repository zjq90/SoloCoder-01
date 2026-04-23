package com.qianzhi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDetail {
    private Long id;
    private Long accountId;
    private Long agentId;
    private Integer type;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private Long relatedId;
    private String description;
    private LocalDateTime createTime;
    
    private String agentName;
    private String typeName;
}
