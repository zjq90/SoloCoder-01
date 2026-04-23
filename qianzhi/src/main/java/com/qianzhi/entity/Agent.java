package com.qianzhi.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Agent {
    private Long id;
    private Long userId;
    private String agentName;
    private String agentCode;
    private Integer agentLevel;
    private Long parentId;
    private String contact;
    private String address;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private List<Agent> children;
    private String parentName;
}
