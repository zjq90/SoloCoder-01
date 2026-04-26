package com.fenzhang.mapper;

import com.fenzhang.entity.FenzhangLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FenzhangLogMapper {

    FenzhangLog findById(@Param("id") Long id);

    FenzhangLog findByLogNo(@Param("logNo") String logNo);

    List<FenzhangLog> findByBatchNo(@Param("batchNo") String batchNo);

    List<FenzhangLog> findByAgentCode(@Param("agentCode") String agentCode);

    List<FenzhangLog> findByOrderNo(@Param("orderNo") String orderNo);

    List<FenzhangLog> findByOperationType(@Param("operationType") String operationType);

    List<FenzhangLog> findAll();

    int insert(FenzhangLog log);

    int update(FenzhangLog log);

    int deleteById(@Param("id") Long id);
}
