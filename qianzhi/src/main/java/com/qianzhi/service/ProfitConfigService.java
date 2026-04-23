package com.qianzhi.service;

import com.qianzhi.dto.ProfitConfigDTO;
import com.qianzhi.entity.Agent;
import com.qianzhi.entity.ProfitConfig;
import com.qianzhi.mapper.AgentMapper;
import com.qianzhi.mapper.ProfitConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProfitConfigService {

    @Autowired
    private ProfitConfigMapper profitConfigMapper;

    @Autowired
    private AgentMapper agentMapper;

    public ProfitConfig getById(Long id) {
        return profitConfigMapper.selectById(id);
    }

    public ProfitConfig getByAgentAndProduct(Long agentId, Long productId) {
        return profitConfigMapper.selectByAgentAndProduct(agentId, productId);
    }

    public List<ProfitConfig> getByAgentId(Long agentId) {
        return profitConfigMapper.selectByAgentId(agentId);
    }

    public List<ProfitConfig> list(Long agentId, Long productId) {
        return profitConfigMapper.selectList(agentId, productId);
    }

    @Transactional
    public void setProfitConfig(ProfitConfigDTO dto, Long operatorAgentId) {
        Agent agent = agentMapper.selectById(dto.getAgentId());
        if (agent == null) {
            throw new IllegalArgumentException("代理商不存在");
        }

        if (!agent.getParentId().equals(operatorAgentId)) {
            throw new IllegalArgumentException("只能设置下级代理商的分润比例");
        }

        ProfitConfig parentConfig = profitConfigMapper.selectByAgentAndProduct(operatorAgentId, dto.getProductId());
        if (parentConfig == null) {
            throw new IllegalArgumentException("上级代理商未配置该产品的分润比例");
        }

        if (dto.getProfitRate().compareTo(parentConfig.getProfitRate()) > 0) {
            throw new IllegalArgumentException("下级代理商分润比例不能高于上级代理商");
        }

        ProfitConfig existing = profitConfigMapper.selectByAgentAndProduct(dto.getAgentId(), dto.getProductId());
        if (existing != null) {
            existing.setProfitRate(dto.getProfitRate());
            profitConfigMapper.update(existing);
        } else {
            ProfitConfig config = new ProfitConfig();
            config.setAgentId(dto.getAgentId());
            config.setProductId(dto.getProductId());
            config.setProfitRate(dto.getProfitRate());
            profitConfigMapper.insert(config);
        }
    }

    @Transactional
    public void updateProfitConfig(ProfitConfig config, Long operatorAgentId) {
        ProfitConfig existing = profitConfigMapper.selectById(config.getId());
        if (existing == null) {
            throw new IllegalArgumentException("分润配置不存在");
        }

        Agent agent = agentMapper.selectById(existing.getAgentId());
        if (!agent.getParentId().equals(operatorAgentId)) {
            throw new IllegalArgumentException("只能修改下级代理商的分润比例");
        }

        ProfitConfig parentConfig = profitConfigMapper.selectByAgentAndProduct(operatorAgentId, existing.getProductId());
        if (parentConfig == null) {
            throw new IllegalArgumentException("上级代理商未配置该产品的分润比例");
        }

        if (config.getProfitRate().compareTo(parentConfig.getProfitRate()) > 0) {
            throw new IllegalArgumentException("下级代理商分润比例不能高于上级代理商");
        }

        existing.setProfitRate(config.getProfitRate());
        profitConfigMapper.update(existing);
    }

    public BigDecimal getParentProfitRate(Long agentId, Long productId) {
        Agent agent = agentMapper.selectById(agentId);
        if (agent != null && agent.getParentId() != null && agent.getParentId() > 0) {
            ProfitConfig parentConfig = profitConfigMapper.selectByAgentAndProduct(agent.getParentId(), productId);
            if (parentConfig != null) {
                return parentConfig.getProfitRate();
            }
        }
        return null;
    }
}
