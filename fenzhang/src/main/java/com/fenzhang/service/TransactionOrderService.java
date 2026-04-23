package com.fenzhang.service;

import com.fenzhang.entity.TransactionOrder;

import java.util.List;

public interface TransactionOrderService {

    TransactionOrder findById(Long id);

    TransactionOrder findByOrderNo(String orderNo);

    List<TransactionOrder> findByProfitStatus(Integer profitStatus);

    List<TransactionOrder> findByAgentCode(String agentCode);

    List<TransactionOrder> findAll();

    int save(TransactionOrder order);

    int update(TransactionOrder order);

    int updateProfitStatus(String orderNo, Integer profitStatus);

    int deleteById(Long id);
}
