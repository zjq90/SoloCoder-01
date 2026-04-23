package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.entity.Machine;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @GetMapping("/{id}")
    public Result<Machine> getById(@PathVariable Long id) {
        Machine machine = machineService.getById(id);
        if (machine == null) {
            return Result.error("机器不存在");
        }
        return Result.success(machine);
    }

    @GetMapping("/sn/{machineSn}")
    public Result<Machine> getBySn(@PathVariable String machineSn) {
        Machine machine = machineService.getBySn(machineSn);
        if (machine == null) {
            return Result.error("机器不存在");
        }
        return Result.success(machine);
    }

    @GetMapping("/my-machines")
    public Result<List<Machine>> getMyMachines(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Machine> machines = machineService.getByAgentId(principal.getAgentId());
        return Result.success(machines);
    }

    @GetMapping("/merchant/{merchantId}")
    public Result<List<Machine>> getByMerchantId(@PathVariable Long merchantId) {
        List<Machine> machines = machineService.getByMerchantId(merchantId);
        return Result.success(machines);
    }

    @GetMapping("/list")
    public Result<List<Machine>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String machineSn,
            @RequestParam(required = false) Integer status) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Machine> list = machineService.list(principal.getAgentId(), merchantId, machineSn, status);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Machine machine, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        machine.setAgentId(principal.getAgentId());
        machineService.add(machine);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Machine machine) {
        machineService.update(machine);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        machineService.delete(id);
        return Result.success();
    }

    @PostMapping("/bind/{id}")
    public Result<Void> bindMachine(@PathVariable Long id, @RequestParam Long merchantId) {
        machineService.bindMachine(id, merchantId);
        return Result.success();
    }

    @PostMapping("/unbind/{id}")
    public Result<Void> unbindMachine(@PathVariable Long id) {
        machineService.unbindMachine(id);
        return Result.success();
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Map<String, Object> stats = machineService.getStats(principal.getAgentId());
        return Result.success(stats);
    }
}
