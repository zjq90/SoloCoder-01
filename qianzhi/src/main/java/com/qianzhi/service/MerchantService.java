package com.qianzhi.service;

import com.qianzhi.entity.Merchant;
import com.qianzhi.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    public Merchant getById(Long id) {
        return merchantMapper.selectById(id);
    }

    public Merchant getByCode(String merchantCode) {
        return merchantMapper.selectByCode(merchantCode);
    }

    public List<Merchant> getByAgentId(Long agentId) {
        return merchantMapper.selectByAgentId(agentId);
    }

    public List<Merchant> list(Long agentId, String merchantName, Integer status) {
        return merchantMapper.selectList(agentId, merchantName, status);
    }

    @Transactional
    public void add(Merchant merchant) {
        Merchant existing = merchantMapper.selectByCode(merchant.getMerchantCode());
        if (existing != null) {
            throw new IllegalArgumentException("商户编码已存在");
        }
        merchantMapper.insert(merchant);
    }

    @Transactional
    public void update(Merchant merchant) {
        Merchant existing = merchantMapper.selectById(merchant.getId());
        if (existing == null) {
            throw new IllegalArgumentException("商户不存在");
        }
        merchantMapper.update(merchant);
    }

    @Transactional
    public void delete(Long id) {
        merchantMapper.deleteById(id);
    }

    public Map<String, Object> getStats(Long agentId) {
        Map<String, Object> stats = new HashMap<>();
        
        long total = merchantMapper.countByAgentId(agentId);
        List<Merchant> merchants = merchantMapper.selectByAgentId(agentId);
        
        long activeCount = merchants.stream()
                .filter(m -> m.getStatus() != null && m.getStatus() == 1)
                .count();
        long inactiveCount = total - activeCount;

        stats.put("totalCount", total);
        stats.put("activeCount", activeCount);
        stats.put("inactiveCount", inactiveCount);

        return stats;
    }
}
