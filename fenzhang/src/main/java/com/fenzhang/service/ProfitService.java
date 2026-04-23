package com.fenzhang.service;

import com.fenzhang.entity.TransactionOrder;

import java.util.List;

public interface ProfitService {

    void calculateProfitForOrder(TransactionOrder order, String batchNo);

    void calculateProfitForOrders(List<TransactionOrder> orders, String batchNo);

    void processProfitBatch();
}
