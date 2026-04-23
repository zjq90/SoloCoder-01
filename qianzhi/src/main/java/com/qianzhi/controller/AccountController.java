package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.dto.WithdrawDTO;
import com.qianzhi.entity.Account;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/info")
    public Result<Account> getMyAccount(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Account account = accountService.getByAgentId(principal.getAgentId());
        if (account == null) {
            return Result.error("账户不存在");
        }
        return Result.success(account);
    }

    @GetMapping("/{id}")
    public Result<Account> getById(@PathVariable Long id) {
        Account account = accountService.getById(id);
        if (account == null) {
            return Result.error("账户不存在");
        }
        return Result.success(account);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Map<String, Object> stats = accountService.getAccountStats(principal.getAgentId());
        return Result.success(stats);
    }

    @PostMapping("/withdraw")
    public Result<Map<String, Object>> withdraw(
            @Validated @RequestBody WithdrawDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Map<String, Object> result = accountService.withdraw(principal.getAgentId(), dto);
        return Result.success(result);
    }
}
