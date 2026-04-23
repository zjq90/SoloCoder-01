package com.qianzhi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Merchant {
    private Long id;
    private Long agentId;
    private String merchantName;
    private String merchantCode;
    private String contactName;
    private String contactPhone;
    private String address;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String agentName;
}
