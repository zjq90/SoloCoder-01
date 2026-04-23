package com.qianzhi.service;

import com.qianzhi.common.PageResult;
import com.qianzhi.entity.ProfitRecord;
import com.qianzhi.mapper.ProfitRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfitRecordService {

    @Autowired
    private ProfitRecordMapper profitRecordMapper;

    public ProfitRecord getById(Long id) {
        return profitRecordMapper.selectById(id);
    }

    public List<ProfitRecord> getByAgentId(Long agentId) {
        return profitRecordMapper.selectByAgentId(agentId);
    }

    public List<ProfitRecord> list(Long agentId, Long productId, Integer status, String startTime, String endTime) {
        return profitRecordMapper.selectList(agentId, productId, status, startTime, endTime);
    }

    public PageResult<ProfitRecord> page(Long agentId, Long current, Long size) {
        if (current == null || current < 1) current = 1L;
        if (size == null || size < 1) size = 10L;

        long total = profitRecordMapper.countByAgentId(agentId);
        long offset = (current - 1) * size;

        List<ProfitRecord> records = profitRecordMapper.selectPage(agentId, offset, size);

        return new PageResult<>(records, total, size, current);
    }

    public Map<String, Object> getStats(Long agentId, String startTime, String endTime) {
        Map<String, Object> stats = new HashMap<>();
        
        BigDecimal totalProfit = profitRecordMapper.sumProfitAmount(agentId, startTime, endTime);
        long totalCount = profitRecordMapper.countByAgentId(agentId);

        stats.put("totalProfit", totalProfit != null ? totalProfit : BigDecimal.ZERO);
        stats.put("totalCount", totalCount);
        stats.put("startTime", startTime);
        stats.put("endTime", endTime);

        return stats;
    }
}
