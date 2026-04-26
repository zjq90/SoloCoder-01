package com.fenzhang.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_PROFIT_IN = "PROFIT_IN";
    public static final String TYPE_WITHDRAW = "WITHDRAW";
    public static final String TYPE_FREEZE = "FREEZE";
    public static final String TYPE_UNFREEZE = "UNFREEZE";

    private Long id;

    private String detailNo;

    private String agentCode;

    private String agentName;

    private String orderNo;

    private String profitNo;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal beforeBalance;

    private BigDecimal afterBalance;

    private String remark;

    private LocalDateTime createTime;
}
