package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.entity.Agent;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/info")
    public Result<Agent> getCurrentAgentInfo(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Agent agent = agentService.getAgentInfo(principal.getAgentId());
        return Result.success(agent);
    }

    @GetMapping("/{id}")
    public Result<Agent> getById(@PathVariable Long id) {
        Agent agent = agentService.getById(id);
        if (agent == null) {
            return Result.error("代理商不存在");
        }
        return Result.success(agent);
    }

    @GetMapping("/children")
    public Result<List<Agent>> getChildren(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Agent> children = agentService.getAllChildren(principal.getAgentId());
        return Result.success(children);
    }

    @GetMapping("/tree")
    public Result<List<Agent>> getTree(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Agent> tree = agentService.getAgentTree(principal.getAgentId());
        return Result.success(tree);
    }

    @GetMapping("/list")
    public Result<List<Agent>> list(
            @RequestParam(required = false) String agentName,
            @RequestParam(required = false) String agentCode,
            @RequestParam(required = false) Integer status) {
        List<Agent> list = agentService.list(agentName, agentCode, status);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Agent agent, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        agent.setParentId(principal.getAgentId());
        agentService.add(agent);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Agent agent) {
        agentService.update(agent);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        agentService.delete(id);
        return Result.success();
    }

    @GetMapping("/sub-agents")
    public Result<Map<String, Object>> getSubAgents(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Long> subAgentIds = agentService.getAllSubAgentIds(principal.getAgentId());
        Map<String, Object> result = new HashMap<>();
        result.put("subAgentIds", subAgentIds);
        result.put("count", subAgentIds.size());
        return Result.success(result);
    }
}
