package com.qianzhi.dto;

import lombok.Data;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProfitConfigDTO {
    @NotNull(message = "代理商ID不能为空")
    private Long agentId;
    
    @NotNull(message = "产品ID不能为空")
    private Long productId;
    
    @NotNull(message = "分润比例不能为空")
    @DecimalMin(value = "0.0000", message = "分润比例不能为负数")
    @DecimalMax(value = "1.0000", message = "分润比例不能超过100%")
    private BigDecimal profitRate;
}
