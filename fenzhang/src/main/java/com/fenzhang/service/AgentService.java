package com.fenzhang.service;

import com.fenzhang.entity.Agent;

import java.util.List;

public interface AgentService {

    Agent findById(Long id);

    Agent findByAgentCode(String agentCode);

    List<Agent> findByParentAgentCode(String parentAgentCode);

    List<Agent> findAll();

    int save(Agent agent);

    int update(Agent agent);

    int deleteById(Long id);

    Agent findPlatform();

    List<Agent> findAllAgents();
}
