package com.qianzhi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProfitConfig {
    private Long id;
    private Long agentId;
    private Long productId;
    private BigDecimal profitRate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String agentName;
    private String productName;
}
