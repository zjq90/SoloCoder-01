package com.qianzhi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Machine {
    private Long id;
    private Long agentId;
    private Long merchantId;
    private String machineSn;
    private String machineName;
    private String machineType;
    private LocalDateTime bindTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String agentName;
    private String merchantName;
}
