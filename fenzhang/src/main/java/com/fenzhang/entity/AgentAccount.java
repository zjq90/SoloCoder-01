package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AgentAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String agentCode;

    private String agentName;

    private BigDecimal balance;

    private BigDecimal totalProfit;

    private BigDecimal withdrawAmount;

    private BigDecimal frozenAmount;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
