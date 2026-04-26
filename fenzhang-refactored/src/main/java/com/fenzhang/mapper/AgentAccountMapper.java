package com.fenzhang.mapper;

import com.fenzhang.entity.AgentAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AgentAccountMapper {

    AgentAccount findById(@Param("id") Long id);

    AgentAccount findByAgentCode(@Param("agentCode") String agentCode);

    List<AgentAccount> findAll();

    int insert(AgentAccount account);

    int update(AgentAccount account);

    int updateBalance(@Param("agentCode") String agentCode, 
                      @Param("balance") BigDecimal balance, 
                      @Param("totalProfit") BigDecimal totalProfit);

    int updateBalanceWithVersion(@Param("agentCode") String agentCode,
                                  @Param("amount") BigDecimal amount,
                                  @Param("oldBalance") BigDecimal oldBalance);

    int deleteById(@Param("id") Long id);
}
