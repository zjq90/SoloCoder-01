package com.qianzhi.service;

import com.qianzhi.entity.Machine;
import com.qianzhi.mapper.MachineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MachineService {

    @Autowired
    private MachineMapper machineMapper;

    public Machine getById(Long id) {
        return machineMapper.selectById(id);
    }

    public Machine getBySn(String machineSn) {
        return machineMapper.selectBySn(machineSn);
    }

    public List<Machine> getByAgentId(Long agentId) {
        return machineMapper.selectByAgentId(agentId);
    }

    public List<Machine> getByMerchantId(Long merchantId) {
        return machineMapper.selectByMerchantId(merchantId);
    }

    public List<Machine> list(Long agentId, Long merchantId, String machineSn, Integer status) {
        return machineMapper.selectList(agentId, merchantId, machineSn, status);
    }

    @Transactional
    public void add(Machine machine) {
        Machine existing = machineMapper.selectBySn(machine.getMachineSn());
        if (existing != null) {
            throw new IllegalArgumentException("机器SN已存在");
        }
        machine.setStatus(0);
        machineMapper.insert(machine);
    }

    @Transactional
    public void update(Machine machine) {
        Machine existing = machineMapper.selectById(machine.getId());
        if (existing == null) {
            throw new IllegalArgumentException("机器不存在");
        }
        machineMapper.update(machine);
    }

    @Transactional
    public void delete(Long id) {
        machineMapper.deleteById(id);
    }

    @Transactional
    public void bindMachine(Long id, Long merchantId) {
        Machine machine = machineMapper.selectById(id);
        if (machine == null) {
            throw new IllegalArgumentException("机器不存在");
        }

        if (machine.getStatus() == 1) {
            throw new IllegalArgumentException("机器已绑定");
        }

        machine.setMerchantId(merchantId);
        machine.setStatus(1);
        machine.setBindTime(LocalDateTime.now());
        machineMapper.update(machine);
    }

    @Transactional
    public void unbindMachine(Long id) {
        Machine machine = machineMapper.selectById(id);
        if (machine == null) {
            throw new IllegalArgumentException("机器不存在");
        }

        if (machine.getStatus() == 0) {
            throw new IllegalArgumentException("机器未绑定");
        }

        machine.setMerchantId(null);
        machine.setStatus(0);
        machine.setBindTime(null);
        machineMapper.update(machine);
    }

    public Map<String, Object> getStats(Long agentId) {
        Map<String, Object> stats = new HashMap<>();
        
        long total = machineMapper.countByAgentId(agentId);
        List<Machine> machines = machineMapper.selectByAgentId(agentId);
        
        long boundCount = machines.stream()
                .filter(m -> m.getStatus() != null && m.getStatus() == 1)
                .count();
        long unboundCount = machines.stream()
                .filter(m -> m.getStatus() != null && m.getStatus() == 0)
                .count();
        long disabledCount = machines.stream()
                .filter(m -> m.getStatus() != null && m.getStatus() == 2)
                .count();

        stats.put("totalCount", total);
        stats.put("boundCount", boundCount);
        stats.put("unboundCount", unboundCount);
        stats.put("disabledCount", disabledCount);

        return stats;
    }
}
