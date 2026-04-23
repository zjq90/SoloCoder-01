package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.dto.ProfitConfigDTO;
import com.qianzhi.entity.ProfitConfig;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.ProfitConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profit-config")
public class ProfitConfigController {

    @Autowired
    private ProfitConfigService profitConfigService;

    @GetMapping("/{id}")
    public Result<ProfitConfig> getById(@PathVariable Long id) {
        ProfitConfig config = profitConfigService.getById(id);
        if (config == null) {
            return Result.error("分润配置不存在");
        }
        return Result.success(config);
    }

    @GetMapping("/my-configs")
    public Result<List<ProfitConfig>> getMyConfigs(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<ProfitConfig> configs = profitConfigService.getByAgentId(principal.getAgentId());
        return Result.success(configs);
    }

    @GetMapping("/agent/{agentId}")
    public Result<List<ProfitConfig>> getByAgentId(@PathVariable Long agentId) {
        List<ProfitConfig> configs = profitConfigService.getByAgentId(agentId);
        return Result.success(configs);
    }

    @GetMapping("/list")
    public Result<List<ProfitConfig>> list(
            @RequestParam(required = false) Long agentId,
            @RequestParam(required = false) Long productId) {
        List<ProfitConfig> list = profitConfigService.list(agentId, productId);
        return Result.success(list);
    }

    @PostMapping("/set")
    public Result<Void> setConfig(
            @Validated @RequestBody ProfitConfigDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        profitConfigService.setProfitConfig(dto, principal.getAgentId());
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateConfig(
            @RequestBody ProfitConfig config,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        profitConfigService.updateProfitConfig(config, principal.getAgentId());
        return Result.success();
    }

    @GetMapping("/parent-rate")
    public Result<Map<String, Object>> getParentRate(
            @RequestParam Long agentId,
            @RequestParam Long productId) {
        BigDecimal rate = profitConfigService.getParentProfitRate(agentId, productId);
        Map<String, Object> result = new HashMap<>();
        result.put("parentProfitRate", rate);
        return Result.success(result);
    }
}
