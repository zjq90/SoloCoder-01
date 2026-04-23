package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FenzhangLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String logNo;

    private String batchNo;

    private String orderNo;

    private String agentCode;

    private String operationType;

    private String logLevel;

    private String message;

    private String detail;

    private LocalDateTime createTime;
}
