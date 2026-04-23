package com.qianzhi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String phone;
    private String password;
    private String loginCode;
    private LocalDateTime codeExpireTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
