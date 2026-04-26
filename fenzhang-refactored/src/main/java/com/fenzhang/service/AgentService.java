package com.fenzhang.service;

import com.fenzhang.entity.Agent;

import java.util.List;

public interface AgentService {

    Agent findById(Long id);

    Agent findByAgentCode(String agentCode);

    List<Agent> findByParentAgentCode(String parentAgentCode);

    List<Agent> findAllAgents();

    List<Agent> findEnabledAgents();

    int saveAgent(Agent agent);

    int updateAgent(Agent agent);

    int deleteAgent(Long id);

    List<Agent> buildAgentChain(String startAgentCode);

    Agent getPlatformAgent();
}
