package com.qianzhi.controller;

import com.qianzhi.common.PageResult;
import com.qianzhi.common.Result;
import com.qianzhi.entity.AccountDetail;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.AccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account-detail")
public class AccountDetailController {

    @Autowired
    private AccountDetailService accountDetailService;

    @GetMapping("/{id}")
    public Result<AccountDetail> getById(@PathVariable Long id) {
        AccountDetail detail = accountDetailService.getById(id);
        if (detail == null) {
            return Result.error("账户明细不存在");
        }
        return Result.success(detail);
    }

    @GetMapping("/my-details")
    public Result<List<AccountDetail>> getMyDetails(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<AccountDetail> details = accountDetailService.getByAgentId(principal.getAgentId());
        return Result.success(details);
    }

    @GetMapping("/list")
    public Result<List<AccountDetail>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<AccountDetail> list = accountDetailService.list(principal.getAgentId(), type, startTime, endTime);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<PageResult<AccountDetail>> page(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "1") Long current,
            @RequestParam(required = false, defaultValue = "10") Long size) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        PageResult<AccountDetail> page = accountDetailService.page(principal.getAgentId(), current, size);
        return Result.success(page);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Map<String, Object> stats = accountDetailService.getStats(principal.getAgentId(), startTime, endTime);
        return Result.success(stats);
    }
}
