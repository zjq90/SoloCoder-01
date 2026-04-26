package com.fenzhang.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProfitCalculateResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer resultCode;
    private String resultMsg;
    private String orderNo;
    private String batchNo;

    public boolean isSuccess() {
        return resultCode != null && resultCode == 0;
    }
}
