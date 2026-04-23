package com.qianzhi.mapper;

import com.qianzhi.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MerchantMapper {
    
    Merchant selectById(@Param("id") Long id);
    
    Merchant selectByCode(@Param("merchantCode") String merchantCode);
    
    List<Merchant> selectByAgentId(@Param("agentId") Long agentId);
    
    List<Merchant> selectList(@Param("agentId") Long agentId,
                               @Param("merchantName") String merchantName,
                               @Param("status") Integer status);
    
    long countByAgentId(@Param("agentId") Long agentId);
    
    int insert(Merchant merchant);
    
    int update(Merchant merchant);
    
    int deleteById(@Param("id") Long id);
}
