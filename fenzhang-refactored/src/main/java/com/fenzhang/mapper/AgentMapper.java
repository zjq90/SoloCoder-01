package com.fenzhang.mapper;

import com.fenzhang.entity.Agent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgentMapper {

    Agent findById(@Param("id") Long id);

    Agent findByAgentCode(@Param("agentCode") String agentCode);

    List<Agent> findByParentAgentCode(@Param("parentAgentCode") String parentAgentCode);

    List<Agent> findAll();

    List<Agent> findByStatus(@Param("status") Integer status);

    int insert(Agent agent);

    int update(Agent agent);

    int deleteById(@Param("id") Long id);
}
