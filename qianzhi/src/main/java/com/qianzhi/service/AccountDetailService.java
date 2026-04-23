package com.qianzhi.service;

import com.qianzhi.common.PageResult;
import com.qianzhi.entity.AccountDetail;
import com.qianzhi.mapper.AccountDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountDetailService {

    @Autowired
    private AccountDetailMapper accountDetailMapper;

    public AccountDetail getById(Long id) {
        AccountDetail detail = accountDetailMapper.selectById(id);
        if (detail != null) {
            detail.setTypeName(getTypeName(detail.getType()));
        }
        return detail;
    }

    public List<AccountDetail> getByAgentId(Long agentId) {
        List<AccountDetail> list = accountDetailMapper.selectByAgentId(agentId);
        for (AccountDetail detail : list) {
            detail.setTypeName(getTypeName(detail.getType()));
        }
        return list;
    }

    public List<AccountDetail> list(Long agentId, Integer type, String startTime, String endTime) {
        List<AccountDetail> list = accountDetailMapper.selectList(agentId, type, startTime, endTime);
        for (AccountDetail detail : list) {
            detail.setTypeName(getTypeName(detail.getType()));
        }
        return list;
    }

    public PageResult<AccountDetail> page(Long agentId, Long current, Long size) {
        if (current == null || current < 1) current = 1L;
        if (size == null || size < 1) size = 10L;

        long total = accountDetailMapper.countByAgentId(agentId);
        long offset = (current - 1) * size;

        List<AccountDetail> records = accountDetailMapper.selectPage(agentId, offset, size);
        for (AccountDetail detail : records) {
            detail.setTypeName(getTypeName(detail.getType()));
        }

        return new PageResult<>(records, total, size, current);
    }

    public Map<String, Object> getStats(Long agentId, String startTime, String endTime) {
        List<AccountDetail> list = accountDetailMapper.selectList(agentId, null, startTime, endTime);
        
        Map<String, Object> stats = new HashMap<>();
        java.math.BigDecimal totalIncome = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalExpense = java.math.BigDecimal.ZERO;
        int incomeCount = 0;
        int expenseCount = 0;

        for (AccountDetail detail : list) {
            if (detail.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                totalIncome = totalIncome.add(detail.getAmount());
                incomeCount++;
            } else {
                totalExpense = totalExpense.add(detail.getAmount().abs());
                expenseCount++;
            }
        }

        stats.put("totalIncome", totalIncome);
        stats.put("totalExpense", totalExpense);
        stats.put("incomeCount", incomeCount);
        stats.put("expenseCount", expenseCount);
        stats.put("totalCount", list.size());

        return stats;
    }

    private String getTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "分润收入";
            case 2: return "提现";
            case 3: return "提现退款";
            case 4: return "其他";
            default: return "未知";
        }
    }
}
