package com.fenzhang.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BatchProfitResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchNo;
    private Integer successCount;
    private Integer failCount;
    private List<String> failedOrders;
    private Long startTime;
    private Long endTime;

    public BatchProfitResult() {
        this.failedOrders = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }

    public void addFailedOrder(String orderNo) {
        if (this.failedOrders == null) {
            this.failedOrders = new ArrayList<>();
        }
        this.failedOrders.add(orderNo);
    }

    public long getDuration() {
        if (endTime != null && startTime != null) {
            return endTime - startTime;
        }
        return 0;
    }
}
