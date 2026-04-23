package com.qianzhi.service;

import com.qianzhi.entity.WithdrawRecord;
import com.qianzhi.mapper.WithdrawRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WithdrawRecordService {

    @Autowired
    private WithdrawRecordMapper withdrawRecordMapper;

    public WithdrawRecord getById(Long id) {
        return withdrawRecordMapper.selectById(id);
    }

    public List<WithdrawRecord> getByAgentId(Long agentId) {
        return withdrawRecordMapper.selectByAgentId(agentId);
    }

    public List<WithdrawRecord> list(Long agentId, Integer status, String startTime, String endTime) {
        return withdrawRecordMapper.selectList(agentId, status, startTime, endTime);
    }

    @Transactional
    public void processWithdraw(Long id, Integer status, String remark) {
        WithdrawRecord record = withdrawRecordMapper.selectById(id);
        if (record == null) {
            throw new IllegalArgumentException("提现记录不存在");
        }

        if (record.getStatus() != 0) {
            throw new IllegalArgumentException("该提现记录已处理");
        }

        record.setStatus(status);
        if (remark != null) {
            record.setRemark(remark);
        }
        withdrawRecordMapper.update(record);
    }

    @Transactional
    public void cancelWithdraw(Long id) {
        WithdrawRecord record = withdrawRecordMapper.selectById(id);
        if (record == null) {
            throw new IllegalArgumentException("提现记录不存在");
        }

        if (record.getStatus() != 0) {
            throw new IllegalArgumentException("该提现记录已处理，无法取消");
        }

        record.setStatus(3);
        record.setRemark("用户取消");
        withdrawRecordMapper.update(record);
    }
}
