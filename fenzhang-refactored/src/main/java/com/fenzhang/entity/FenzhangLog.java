package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FenzhangLog implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";

    public static final String OP_BATCH_START = "BATCH_START";
    public static final String OP_BATCH_END = "BATCH_END";
    public static final String OP_CALCULATE_PROFIT = "CALCULATE_PROFIT";
    public static final String OP_ORDER_FAILED = "ORDER_FAILED";
    public static final String OP_ORDER_SKIP = "ORDER_SKIP";
    public static final String OP_ACCOUNT_UPDATE = "ACCOUNT_UPDATE";
    public static final String OP_STATUS_UPDATE_FAILED = "STATUS_UPDATE_FAILED";

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
