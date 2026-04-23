package com.qianzhi.security;

import lombok.Data;

@Data
public class UserPrincipal {
    private Long userId;
    private String phone;
    private Long agentId;
    private Integer agentLevel;
}
