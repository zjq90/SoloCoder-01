package com.qianzhi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WithdrawRecord {
    private Long id;
    private Long accountId;
    private Long agentId;
    private BigDecimal amount;
    private BigDecimal feeRate;
    private BigDecimal feeAmount;
    private BigDecimal actualAmount;
    private String bankName;
    private String bankAccount;
    private String bankHolder;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String agentName;
}
