package com.fenzhang.service.impl;

import com.fenzhang.entity.Agent;
import com.fenzhang.mapper.AgentMapper;
import com.fenzhang.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    private static final String AGENT_CACHE_PREFIX = "agent:";

    @Resource
    private AgentMapper agentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Agent findById(Long id) {
        String cacheKey = AGENT_CACHE_PREFIX + "id:" + id;
        Agent agent = (Agent) redisTemplate.opsForValue().get(cacheKey);
        if (agent != null) {
            return agent;
        }
        agent = agentMapper.findById(id);
        if (agent != null) {
            redisTemplate.opsForValue().set(cacheKey, agent, 10, TimeUnit.MINUTES);
        }
        return agent;
    }

    @Override
    public Agent findByAgentCode(String agentCode) {
        String cacheKey = AGENT_CACHE_PREFIX + "code:" + agentCode;
        Agent agent = (Agent) redisTemplate.opsForValue().get(cacheKey);
        if (agent != null) {
            return agent;
        }
        agent = agentMapper.findByAgentCode(agentCode);
        if (agent != null) {
            redisTemplate.opsForValue().set(cacheKey, agent, 10, TimeUnit.MINUTES);
        }
        return agent;
    }

    @Override
    public List<Agent> findByParentAgentCode(String parentAgentCode) {
        return agentMapper.findByParentAgentCode(parentAgentCode);
    }

    @Override
    public List<Agent> findAll() {
        return agentMapper.findAll();
    }

    @Override
    public int save(Agent agent) {
        clearAgentCache(agent);
        return agentMapper.insert(agent);
    }

    @Override
    public int update(Agent agent) {
        clearAgentCache(agent);
        return agentMapper.update(agent);
    }

    @Override
    public int deleteById(Long id) {
        Agent agent = findById(id);
        if (agent != null) {
            clearAgentCache(agent);
        }
        return agentMapper.deleteById(id);
    }

    @Override
    public Agent findPlatform() {
        return findByAgentCode("PLATFORM");
    }

    @Override
    public List<Agent> findAllAgents() {
        List<Agent> allAgents = agentMapper.findAll();
        return allAgents.stream()
                .filter(agent -> !"PLATFORM".equals(agent.getAgentCode()))
                .collect(java.util.stream.Collectors.toList());
    }

    private void clearAgentCache(Agent agent) {
        if (agent.getId() != null) {
            redisTemplate.delete(AGENT_CACHE_PREFIX + "id:" + agent.getId());
        }
        if (agent.getAgentCode() != null) {
            redisTemplate.delete(AGENT_CACHE_PREFIX + "code:" + agent.getAgentCode());
        }
    }
}
