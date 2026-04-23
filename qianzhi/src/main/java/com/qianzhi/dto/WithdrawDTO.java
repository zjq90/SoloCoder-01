package com.qianzhi.dto;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WithdrawDTO {
    @NotNull(message = "提现金额不能为空")
    @DecimalMin(value = "100.00", message = "提现金额最低100元")
    private BigDecimal amount;
    
    @NotBlank(message = "银行名称不能为空")
    private String bankName;
    
    @NotBlank(message = "银行账号不能为空")
    private String bankAccount;
    
    @NotBlank(message = "开户人姓名不能为空")
    private String bankHolder;
}
