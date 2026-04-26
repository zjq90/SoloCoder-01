package com.fenzhang.service;

import com.fenzhang.dto.BatchProfitResult;
import com.fenzhang.dto.ProfitCalculateResult;

public interface ProfitService {

    ProfitCalculateResult calculateProfitForOrder(String orderNo, String batchNo);

    BatchProfitResult calculateProfitForBatch(String batchNo);

    BatchProfitResult processProfitBatch();

    int countPendingOrders();

    String getPlatformAgentCode();
}
