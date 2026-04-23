package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.entity.Merchant;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @GetMapping("/{id}")
    public Result<Merchant> getById(@PathVariable Long id) {
        Merchant merchant = merchantService.getById(id);
        if (merchant == null) {
            return Result.error("商户不存在");
        }
        return Result.success(merchant);
    }

    @GetMapping("/my-merchants")
    public Result<List<Merchant>> getMyMerchants(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Merchant> merchants = merchantService.getByAgentId(principal.getAgentId());
        return Result.success(merchants);
    }

    @GetMapping("/list")
    public Result<List<Merchant>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) Integer status) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<Merchant> list = merchantService.list(principal.getAgentId(), merchantName, status);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Merchant merchant, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        merchant.setAgentId(principal.getAgentId());
        merchantService.add(merchant);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Merchant merchant) {
        merchantService.update(merchant);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        merchantService.delete(id);
        return Result.success();
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        Map<String, Object> stats = merchantService.getStats(principal.getAgentId());
        return Result.success(stats);
    }
}
