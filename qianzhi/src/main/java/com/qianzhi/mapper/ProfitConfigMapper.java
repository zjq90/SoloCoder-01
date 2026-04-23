package com.qianzhi.mapper;

import com.qianzhi.entity.ProfitConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfitConfigMapper {
    
    ProfitConfig selectById(@Param("id") Long id);
    
    ProfitConfig selectByAgentAndProduct(@Param("agentId") Long agentId, 
                                          @Param("productId") Long productId);
    
    List<ProfitConfig> selectByAgentId(@Param("agentId") Long agentId);
    
    List<ProfitConfig> selectList(@Param("agentId") Long agentId,
                                   @Param("productId") Long productId);
    
    int insert(ProfitConfig profitConfig);
    
    int update(ProfitConfig profitConfig);
    
    int deleteById(@Param("id") Long id);
}
