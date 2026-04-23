package com.jiaoyi.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String channelCode;
    private String channelName;
    private Integer status;
    private Long maxTransactions;
    private Long currentTransactions;
    private Integer priority;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isAvailable() {
        if (status == null || status != 1) {
            return false;
        }
        if (maxTransactions == null || currentTransactions == null) {
            return false;
        }
        return currentTransactions < maxTransactions;
    }
}
