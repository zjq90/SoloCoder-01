package com.qianzhi.mapper;

import com.qianzhi.entity.ProfitRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProfitRecordMapper {
    
    ProfitRecord selectById(@Param("id") Long id);
    
    List<ProfitRecord> selectByAgentId(@Param("agentId") Long agentId);
    
    List<ProfitRecord> selectList(@Param("agentId") Long agentId,
                                   @Param("productId") Long productId,
                                   @Param("status") Integer status,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime);
    
    BigDecimal sumProfitAmount(@Param("agentId") Long agentId,
                                @Param("startTime") String startTime,
                                @Param("endTime") String endTime);
    
    long countByAgentId(@Param("agentId") Long agentId);
    
    List<ProfitRecord> selectPage(@Param("agentId") Long agentId,
                                   @Param("offset") Long offset,
                                   @Param("size") Long size);
    
    int insert(ProfitRecord profitRecord);
}
