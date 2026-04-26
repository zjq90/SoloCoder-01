package com.fenzhang.service;

import com.fenzhang.entity.AgentAccount;

import java.math.BigDecimal;
import java.util.List;

public interface AgentAccountService {

    AgentAccount findById(Long id);

    AgentAccount findByAgentCode(String agentCode);

    List<AgentAccount> findAll();

    int save(AgentAccount account);

    int update(AgentAccount account);

    int updateBalance(String agentCode, BigDecimal balance, BigDecimal totalProfit);

    boolean addProfit(String agentCode, BigDecimal amount, String orderNo, String profitNo, String batchNo);

    int deleteById(Long id);
}
