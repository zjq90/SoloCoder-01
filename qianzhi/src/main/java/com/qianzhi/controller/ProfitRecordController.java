package com.qianzhi.controller;

import com.qianzhi.common.PageResult;
import com.qianzhi.common.Result;
import com.qianzhi.entity.ProfitRecord;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.ProfitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profit-record")
public class ProfitRecordController {

    @Autowired
    private ProfitRecordService profitRecordService;

    @GetMapping("/{id}")
    public Result<ProfitRecord> getById(@PathVariable Long id) {
        ProfitRecord record = profitRecordService.getById(id);
        if (record == null) {
            return Result.error("分润记录不存在");
        }
        return Result.success(record);
    }

    @GetMapping("/my-records")
    public Result<List<ProfitRecord>> getMyRecords(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<ProfitRecord> records = profitRecordService.getByAgentId(principal.getAgentId());
        return Result.success(records);
    }

    @GetMapping("/list")
    public Result<List<ProfitRecord>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        List<ProfitRecord> list = profitRecordService.list(principal.getAgentId(), productId, status, startTime, endTime);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<PageResult<ProfitRecord>> page(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "1") Long current,
            @RequestParam(required = false, defaultValue = "10") Long size) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        PageResult<ProfitRecord> page = profitRecordService.page(principal.getAgentId(), current, size);
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
        Map<String, Object> stats = profitRecordService.getStats(principal.getAgentId(), startTime, endTime);
        return Result.success(stats);
    }
}
