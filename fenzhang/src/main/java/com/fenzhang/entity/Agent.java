package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String agentCode;

    private String agentName;

    private Long parentId;

    private String parentAgentCode;

    private Integer agentLevel;

    private BigDecimal profitRate;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
