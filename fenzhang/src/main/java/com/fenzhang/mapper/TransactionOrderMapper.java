package com.fenzhang.mapper;

import com.fenzhang.entity.TransactionOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionOrderMapper {

    TransactionOrder findById(@Param("id") Long id);

    TransactionOrder findByOrderNo(@Param("orderNo") String orderNo);

    List<TransactionOrder> findByProfitStatus(@Param("profitStatus") Integer profitStatus);

    List<TransactionOrder> findByAgentCode(@Param("agentCode") String agentCode);

    List<TransactionOrder> findAll();

    int insert(TransactionOrder order);

    int update(TransactionOrder order);

    int updateProfitStatus(@Param("orderNo") String orderNo, @Param("profitStatus") Integer profitStatus);

    int deleteById(@Param("id") Long id);
}
