package com.fenzhang.service.impl;

import com.fenzhang.entity.Agent;
import com.fenzhang.mapper.AgentMapper;
import com.fenzhang.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    private static final String PLATFORM_AGENT_CODE = "PLATFORM";
    private static final String REDIS_AGENT_KEY_PREFIX = "fenzhang:agent:";
    private static final long REDIS_EXPIRE_SECONDS = 3600;

    @Resource
    private AgentMapper agentMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Agent findById(Long id) {
        logger.debug("根据ID查询代理商: {}", id);
        return agentMapper.findById(id);
    }

    @Override
    public Agent findByAgentCode(String agentCode) {
        logger.debug("根据代码查询代理商: {}", agentCode);
        
        String cacheKey = REDIS_AGENT_KEY_PREFIX + agentCode;
        Agent cachedAgent = (Agent) redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedAgent != null) {
            logger.debug("从缓存获取代理商: {}", agentCode);
            return cachedAgent;
        }
        
        Agent agent = agentMapper.findByAgentCode(agentCode);
        if (agent != null) {
            redisTemplate.opsForValue().set(cacheKey, agent, REDIS_EXPIRE_SECONDS, TimeUnit.SECONDS);
            logger.debug("缓存代理商: {}", agentCode);
        }
        
        return agent;
    }

    @Override
    public List<Agent> findByParentAgentCode(String parentAgentCode) {
        logger.debug("根据上级代码查询代理商: {}", parentAgentCode);
        return agentMapper.findByParentAgentCode(parentAgentCode);
    }

    @Override
    public List<Agent> findAllAgents() {
        logger.debug("查询所有代理商");
        return agentMapper.findAll();
    }

    @Override
    public List<Agent> findEnabledAgents() {
        logger.debug("查询所有启用的代理商");
        return agentMapper.findByStatus(Agent.STATUS_ENABLED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveAgent(Agent agent) {
        logger.info("保存代理商: {}", agent.getAgentCode());
        
        int result = agentMapper.insert(agent);
        
        if (result > 0) {
            clearAgentCache(agent.getAgentCode());
            logger.info("代理商保存成功: {}", agent.getAgentCode());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAgent(Agent agent) {
        logger.info("更新代理商: {}", agent.getAgentCode());
        
        int result = agentMapper.update(agent);
        
        if (result > 0) {
            clearAgentCache(agent.getAgentCode());
            logger.info("代理商更新成功: {}", agent.getAgentCode());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgent(Long id) {
        logger.info("删除代理商: {}", id);
        
        Agent agent = agentMapper.findById(id);
        if (agent != null) {
            clearAgentCache(agent.getAgentCode());
        }
        
        return agentMapper.deleteById(id);
    }

    @Override
    public List<Agent> buildAgentChain(String startAgentCode) {
        logger.debug("构建代理链，起始代理商: {}", startAgentCode);
        
        List<Agent> chain = new ArrayList<>();
        Agent current = findByAgentCode(startAgentCode);

        while (current != null && !PLATFORM_AGENT_CODE.equals(current.getAgentCode())) {
            chain.add(current);
            if (current.getParentAgentCode() == null || PLATFORM_AGENT_CODE.equals(current.getParentAgentCode())) {
                break;
            }
            current = findByAgentCode(current.getParentAgentCode());
        }

        logger.debug("代理链构建完成，长度: {}", chain.size());
        return chain;
    }

    @Override
    public Agent getPlatformAgent() {
        logger.debug("获取平台代理商");
        return findByAgentCode(PLATFORM_AGENT_CODE);
    }

    private void clearAgentCache(String agentCode) {
        String cacheKey = REDIS_AGENT_KEY_PREFIX + agentCode;
        redisTemplate.delete(cacheKey);
        logger.debug("清除代理商缓存: {}", agentCode);
    }
}
