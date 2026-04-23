package com.qianzhi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account {
    private Long id;
    private Long agentId;
    private BigDecimal balance;
    private BigDecimal frozenAmount;
    private BigDecimal totalIncome;
    private BigDecimal totalWithdraw;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String agentName;
    private String agentCode;
}
