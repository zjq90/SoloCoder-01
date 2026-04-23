package com.jiaoyi.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Merchant implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String clientNo;
    private String merchantName;
    private Integer status;
    private String publicKey;
    private String privateKey;
    private BigDecimal balance;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isActive() {
        return status != null && status == 1;
    }
}
