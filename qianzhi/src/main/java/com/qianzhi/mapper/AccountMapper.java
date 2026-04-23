package com.qianzhi.mapper;

import com.qianzhi.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountMapper {
    
    Account selectById(@Param("id") Long id);
    
    Account selectByAgentId(@Param("agentId") Long agentId);
    
    List<Account> selectList(@Param("agentId") Long agentId);
    
    int insert(Account account);
    
    int update(Account account);
    
    int updateBalance(@Param("id") Long id, 
                      @Param("balanceChange") java.math.BigDecimal balanceChange,
                      @Param("totalIncomeChange") java.math.BigDecimal totalIncomeChange,
                      @Param("totalWithdrawChange") java.math.BigDecimal totalWithdrawChange);
}
