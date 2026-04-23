package com.qianzhi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProfitRecord {
    private Long id;
    private Long agentId;
    private Long productId;
    private BigDecimal transactionAmount;
    private BigDecimal profitRate;
    private BigDecimal profitAmount;
    private Long merchantId;
    private Long machineId;
    private String transactionNo;
    private Integer status;
    private LocalDateTime createTime;
    
    private String agentName;
    private String productName;
    private String merchantName;
    private String machineSn;
}
