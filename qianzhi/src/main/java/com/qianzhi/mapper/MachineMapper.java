package com.qianzhi.mapper;

import com.qianzhi.entity.Machine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MachineMapper {
    
    Machine selectById(@Param("id") Long id);
    
    Machine selectBySn(@Param("machineSn") String machineSn);
    
    List<Machine> selectByAgentId(@Param("agentId") Long agentId);
    
    List<Machine> selectByMerchantId(@Param("merchantId") Long merchantId);
    
    List<Machine> selectList(@Param("agentId") Long agentId,
                              @Param("merchantId") Long merchantId,
                              @Param("machineSn") String machineSn,
                              @Param("status") Integer status);
    
    long countByAgentId(@Param("agentId") Long agentId);
    
    int insert(Machine machine);
    
    int update(Machine machine);
    
    int deleteById(@Param("id") Long id);
}
