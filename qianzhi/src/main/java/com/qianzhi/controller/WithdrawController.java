package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.entity.WithdrawRecord;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.WithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawRecordService withdrawRecordService;

    @GetMapping("/{id}")
    public Result<WithdrawRecord> getById(@PathVariable Long id) {
        WithdrawRecord record = withdrawRecordService.getById(id);
        if (record == null) {
            return Result.error("提现记录不存在");
        }
        return Result.success(record);
    }

    @GetMapping("/my-records")
    public Result<List<WithdrawRecord>> getMyRecords(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<WithdrawRecord> records = withdrawRecordService.getByAgentId(principal.getAgentId());
        return Result.success(records);
    }

    @GetMapping("/list")
    public Result<List<WithdrawRecord>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<WithdrawRecord> list = withdrawRecordService.list(principal.getAgentId(), status, startTime, endTime);
        return Result.success(list);
    }

    @PutMapping("/process/{id}")
    public Result<Void> processWithdraw(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        withdrawRecordService.processWithdraw(id, status, remark);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancelWithdraw(@PathVariable Long id) {
        withdrawRecordService.cancelWithdraw(id);
        return Result.success();
    }
}
