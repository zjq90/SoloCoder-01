package com.qianzhi.mapper;

import com.qianzhi.entity.Agent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgentMapper {
    
    Agent selectById(@Param("id") Long id);
    
    Agent selectByUserId(@Param("userId") Long userId);
    
    List<Agent> selectByParentId(@Param("parentId") Long parentId);
    
    List<Agent> selectByLevel(@Param("level") Integer level);
    
    List<Agent> selectAll();
    
    List<Agent> selectList(@Param("agentName") String agentName, 
                            @Param("agentCode") String agentCode,
                            @Param("status") Integer status);
    
    int insert(Agent agent);
    
    int update(Agent agent);
    
    int deleteById(@Param("id") Long id);
}
