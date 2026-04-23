package com.qianzhi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String productName;
    private String productCode;
    private String description;
    private BigDecimal baseProfitRate;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
