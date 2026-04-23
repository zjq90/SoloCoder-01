package com.jiaoyi.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChannelRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long transactionId;
    private String channelCode;
    private String channelOrderNo;
    private String requestData;
    private String responseData;
    private String responseCode;
    private String responseMsg;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_REQUESTING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;
}
