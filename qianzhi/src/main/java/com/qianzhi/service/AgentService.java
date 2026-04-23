package com.qianzhi.service;

import com.qianzhi.entity.Agent;
import com.qianzhi.entity.ProfitConfig;
import com.qianzhi.mapper.AgentMapper;
import com.qianzhi.mapper.ProfitConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentService {

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private ProfitConfigMapper profitConfigMapper;

    public Agent getById(Long id) {
        return agentMapper.selectById(id);
    }

    public Agent getByUserId(Long userId) {
        return agentMapper.selectByUserId(userId);
    }

    public List<Agent> getAllChildren(Long parentId) {
        return agentMapper.selectByParentId(parentId);
    }

    public List<Agent> getAgentTree(Long parentId) {
        List<Agent> agents = agentMapper.selectByParentId(parentId);
        if (agents != null && !agents.isEmpty()) {
            for (Agent agent : agents) {
                agent.setChildren(getAgentTree(agent.getId()));
            }
        }
        return agents != null ? agents : new ArrayList<>();
    }

    public List<Agent> list(String agentName, String agentCode, Integer status) {
        return agentMapper.selectList(agentName, agentCode, status);
    }

    @Transactional
    public void add(Agent agent) {
        if (agent.getParentId() != null && agent.getParentId() > 0) {
            Agent parent = agentMapper.selectById(agent.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("上级代理不存在");
            }
            agent.setAgentLevel(parent.getAgentLevel() + 1);
            if (agent.getAgentLevel() > 2) {
                throw new IllegalArgumentException("最多只支持两级代理");
            }
        } else {
            agent.setParentId(0L);
            agent.setAgentLevel(1);
        }

        agentMapper.insert(agent);
    }

    @Transactional
    public void update(Agent agent) {
        Agent existing = agentMapper.selectById(agent.getId());
        if (existing == null) {
            throw new IllegalArgumentException("代理不存在");
        }
        agentMapper.update(agent);
    }

    @Transactional
    public void delete(Long id) {
        List<Agent> children = agentMapper.selectByParentId(id);
        if (children != null && !children.isEmpty()) {
            throw new IllegalArgumentException("该代理存在下级代理，无法删除");
        }
        agentMapper.deleteById(id);
    }

    public Agent getAgentInfo(Long agentId) {
        Agent agent = agentMapper.selectById(agentId);
        if (agent != null) {
            List<Agent> children = agentMapper.selectByParentId(agentId);
            agent.setChildren(children);
        }
        return agent;
    }

    public Long getParentAgentId(Long agentId) {
        Agent agent = agentMapper.selectById(agentId);
        if (agent != null && agent.getParentId() != null && agent.getParentId() > 0) {
            return agent.getParentId();
        }
        return null;
    }

    public List<Long> getAllSubAgentIds(Long parentId) {
        List<Long> result = new ArrayList<>();
        collectSubAgentIds(parentId, result);
        return result;
    }

    private void collectSubAgentIds(Long parentId, List<Long> result) {
        List<Agent> children = agentMapper.selectByParentId(parentId);
        if (children != null && !children.isEmpty()) {
            for (Agent child : children) {
                result.add(child.getId());
                collectSubAgentIds(child.getId(), result);
            }
        }
    }
}
