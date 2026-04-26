package com.fenzhang.controller;

import com.fenzhang.dto.BatchProfitResult;
import com.fenzhang.dto.ProfitCalculateResult;
import com.fenzhang.entity.*;
import com.fenzhang.service.*;
import com.fenzhang.util.NoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fenzhang.mapper.FenzhangLogMapper;
import com.fenzhang.mapper.TransactionOrderMapper;

@RestController
@RequestMapping("/api/fenzhang")
public class FenzhangController {

    private static final Logger logger = LoggerFactory.getLogger(FenzhangController.class);

    @Autowired
    private ProfitService profitService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentAccountService accountService;

    @Autowired
    private StatementService statementService;

    @Resource
    private TransactionOrderMapper orderMapper;

    @Resource
    private FenzhangLogMapper fenzhangLogMapper;

    @GetMapping("/test")
    public Map<String, Object> test() {
        logger.info("收到测试请求");
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "分账系统重构版运行正常");
        result.put("version", "2.0.0");
        result.put("feature", "分润计算使用数据库存储过程");
        result.put("time", LocalDate.now().toString());
        logger.info("测试请求处理完成");
        return result;
    }

    @PostMapping("/calculate")
    public Map<String, Object> calculateProfit() {
        logger.info("========== 收到手动分润计算请求 ==========");
        Map<String, Object> result = new HashMap<>();
        
        try {
            int pendingCount = profitService.countPendingOrders();
            logger.info("待处理订单数量: {}", pendingCount);
            
            if (pendingCount == 0) {
                result.put("status", "success");
                result.put("message", "没有待计算分润的订单");
                result.put("orderCount", 0);
                logger.info("没有待处理订单，返回成功");
                return result;
            }

            BatchProfitResult batchResult = profitService.processProfitBatch();
            
            result.put("status", "success");
            result.put("message", "分润计算完成");
            result.put("batchNo", batchResult.getBatchNo());
            result.put("successCount", batchResult.getSuccessCount());
            result.put("failCount", batchResult.getFailCount());
            result.put("durationMs", batchResult.getDuration());
            
            logger.info("========== 分润计算完成，成功: {}，失败: {}，耗时: {}ms ==========", 
                    batchResult.getSuccessCount(), batchResult.getFailCount(), batchResult.getDuration());

        } catch (Exception e) {
            logger.error("========== 手动分润计算失败 ==========", e);
            result.put("status", "error");
            result.put("message", "分润计算失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/calculate/order/{orderNo}")
    public Map<String, Object> calculateProfitForOrder(@PathVariable String orderNo) {
        logger.info("========== 收到单订单分润计算请求，订单号: {} ==========", orderNo);
        Map<String, Object> result = new HashMap<>();
        
        try {
            TransactionOrder order = orderMapper.findByOrderNo(orderNo);
            if (order == null) {
                result.put("status", "error");
                result.put("message", "订单不存在: " + orderNo);
                logger.warn("订单不存在: {}", orderNo);
                return result;
            }

            if (order.getProfitStatus() == TransactionOrder.PROFIT_STATUS_SUCCESS) {
                result.put("status", "error");
                result.put("message", "订单已完成分润，无需重复计算");
                result.put("currentStatus", order.getProfitStatus());
                logger.warn("订单已完成分润: {}", orderNo);
                return result;
            }

            String batchNo = NoGenerator.generateBatchNo();
            ProfitCalculateResult calcResult = profitService.calculateProfitForOrder(orderNo, batchNo);
            
            result.put("status", calcResult.isSuccess() ? "success" : "error");
            result.put("message", calcResult.getResultMsg());
            result.put("orderNo", orderNo);
            result.put("batchNo", batchNo);
            result.put("resultCode", calcResult.getResultCode());
            
            if (calcResult.isSuccess()) {
                logger.info("========== 订单[{}]分润计算成功 ==========", orderNo);
            } else {
                logger.warn("========== 订单[{}]分润计算失败: {} ==========", orderNo, calcResult.getResultMsg());
            }

        } catch (Exception e) {
            logger.error("========== 订单[{}]分润计算异常 ==========", orderNo, e);
            result.put("status", "error");
            result.put("message", "分润计算失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/agents")
    public Map<String, Object> getAgents() {
        logger.debug("获取代理商列表");
        Map<String, Object> result = new HashMap<>();
        try {
            List<Agent> agents = agentService.findAllAgents();
            result.put("status", "success");
            result.put("count", agents.size());
            result.put("data", agents);
            logger.debug("获取到代理商数量: {}", agents.size());
        } catch (Exception e) {
            logger.error("获取代理商列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders")
    public Map<String, Object> getOrders(@RequestParam(required = false) Integer profitStatus) {
        logger.debug("获取订单列表，状态: {}", profitStatus);
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> orders;
            if (profitStatus != null) {
                orders = orderMapper.findByProfitStatus(profitStatus);
            } else {
                orders = orderMapper.findAll();
            }
            result.put("status", "success");
            result.put("count", orders.size());
            result.put("data", orders);
            logger.debug("获取到订单数量: {}", orders.size());
        } catch (Exception e) {
            logger.error("获取订单列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/accounts")
    public Map<String, Object> getAccounts() {
        logger.debug("获取账户列表");
        Map<String, Object> result = new HashMap<>();
        try {
            List<AgentAccount> accounts = accountService.findAll();
            result.put("status", "success");
            result.put("count", accounts.size());
            result.put("data", accounts);
            logger.debug("获取到账户数量: {}", accounts.size());
        } catch (Exception e) {
            logger.error("获取账户列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/statement/daily")
    public Map<String, Object> generateDailyStatement(@RequestParam(required = false) String date) {
        logger.info("========== 收到生成日报表请求，日期: {} ==========", date);
        Map<String, Object> result = new HashMap<>();
        try {
            if (date == null) {
                date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            statementService.generateDailyStatement(date);
            result.put("status", "success");
            result.put("message", "日报表生成完成");
            result.put("date", date);
            logger.info("========== 日报表生成完成，日期: {} ==========", date);
        } catch (Exception e) {
            logger.error("========== 生成日报表失败 ==========", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/statements")
    public Map<String, Object> getStatements(@RequestParam(required = false) String period) {
        logger.debug("获取对账单列表，期间: {}", period);
        Map<String, Object> result = new HashMap<>();
        try {
            List<Statement> statements;
            if (period != null) {
                statements = statementService.findByStatementPeriod(period);
            } else {
                statements = statementService.findAll();
            }
            result.put("status", "success");
            result.put("count", statements.size());
            result.put("data", statements);
            logger.debug("获取到对账单数量: {}", statements.size());
        } catch (Exception e) {
            logger.error("获取对账单列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders/failed")
    public Map<String, Object> getFailedOrders() {
        logger.debug("获取失败订单列表");
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> failedOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_FAILED);
            result.put("status", "success");
            result.put("count", failedOrders.size());
            result.put("data", failedOrders);
            result.put("statusDescription", "订单状态说明: 0=待处理, 1=成功, 2=失败");
            logger.debug("获取到失败订单数量: {}", failedOrders.size());
        } catch (Exception e) {
            logger.error("获取失败订单列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders/pending")
    public Map<String, Object> getPendingOrders() {
        logger.debug("获取待处理订单列表");
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> pendingOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_PENDING);
            result.put("status", "success");
            result.put("count", pendingOrders.size());
            result.put("data", pendingOrders);
            logger.debug("获取到待处理订单数量: {}", pendingOrders.size());
        } catch (Exception e) {
            logger.error("获取待处理订单列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders/success")
    public Map<String, Object> getSuccessOrders() {
        logger.debug("获取成功订单列表");
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> successOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_SUCCESS);
            result.put("status", "success");
            result.put("count", successOrders.size());
            result.put("data", successOrders);
            logger.debug("获取到成功订单数量: {}", successOrders.size());
        } catch (Exception e) {
            logger.error("获取成功订单列表失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/order/retry/{orderNo}")
    public Map<String, Object> retryOrder(@PathVariable String orderNo) {
        logger.info("========== 收到订单重试请求，订单号: {} ==========", orderNo);
        Map<String, Object> result = new HashMap<>();
        try {
            TransactionOrder order = orderMapper.findByOrderNo(orderNo);
            if (order == null) {
                result.put("status", "error");
                result.put("message", "订单不存在: " + orderNo);
                logger.warn("订单不存在: {}", orderNo);
                return result;
            }

            if (order.getProfitStatus() != TransactionOrder.PROFIT_STATUS_FAILED) {
                result.put("status", "error");
                result.put("message", "订单状态不是失败状态，当前状态: " + order.getProfitStatus());
                logger.warn("订单状态不是失败状态: {}", orderNo);
                return result;
            }

            orderMapper.updateProfitStatus(orderNo, TransactionOrder.PROFIT_STATUS_PENDING);
            logger.info("订单状态重置为待处理: {}", orderNo);
            
            String batchNo = NoGenerator.generateBatchNo();
            ProfitCalculateResult calcResult = profitService.calculateProfitForOrder(orderNo, batchNo);

            TransactionOrder updatedOrder = orderMapper.findByOrderNo(orderNo);
            result.put("status", calcResult.isSuccess() ? "success" : "error");
            result.put("message", calcResult.isSuccess() ? "订单重试完成" : "订单重试失败: " + calcResult.getResultMsg());
            result.put("orderNo", orderNo);
            result.put("newStatus", updatedOrder.getProfitStatus());
            result.put("batchNo", batchNo);

            if (calcResult.isSuccess()) {
                logger.info("========== 订单[{}]重试成功 ==========", orderNo);
            } else {
                logger.warn("========== 订单[{}]重试失败: {} ==========", orderNo, calcResult.getResultMsg());
            }

        } catch (Exception e) {
            logger.error("========== 订单[{}]重试异常 ==========", orderNo, e);
            result.put("status", "error");
            result.put("message", "订单重试失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/orders/retry-all-failed")
    public Map<String, Object> retryAllFailedOrders() {
        logger.info("========== 收到批量重试失败订单请求 ==========");
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> failedOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_FAILED);
            
            if (failedOrders.isEmpty()) {
                result.put("status", "success");
                result.put("message", "没有失败的订单需要重试");
                result.put("count", 0);
                logger.info("没有失败的订单需要重试");
                return result;
            }

            for (TransactionOrder order : failedOrders) {
                orderMapper.updateProfitStatus(order.getOrderNo(), TransactionOrder.PROFIT_STATUS_PENDING);
            }
            logger.info("已将 {} 个失败订单重置为待处理状态", failedOrders.size());

            BatchProfitResult batchResult = profitService.processProfitBatch();

            result.put("status", "success");
            result.put("message", "失败订单批量重试已触发");
            result.put("batchNo", batchResult.getBatchNo());
            result.put("totalRetryCount", failedOrders.size());
            result.put("successCount", batchResult.getSuccessCount());
            result.put("failCount", batchResult.getFailCount());
            result.put("durationMs", batchResult.getDuration());

            logger.info("========== 批量重试完成，成功: {}，失败: {}，耗时: {}ms ==========", 
                    batchResult.getSuccessCount(), batchResult.getFailCount(), batchResult.getDuration());

        } catch (Exception e) {
            logger.error("========== 批量重试失败订单异常 ==========", e);
            result.put("status", "error");
            result.put("message", "批量重试失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/logs/batch/{batchNo}")
    public Map<String, Object> getLogsByBatchNo(@PathVariable String batchNo) {
        logger.debug("获取批次日志，批次号: {}", batchNo);
        Map<String, Object> result = new HashMap<>();
        try {
            List<FenzhangLog> logs = fenzhangLogMapper.findByBatchNo(batchNo);
            result.put("status", "success");
            result.put("count", logs.size());
            result.put("data", logs);
            logger.debug("获取到批次日志数量: {}", logs.size());
        } catch (Exception e) {
            logger.error("获取批次日志失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/logs/order/{orderNo}")
    public Map<String, Object> getLogsByOrderNo(@PathVariable String orderNo) {
        logger.debug("获取订单日志，订单号: {}", orderNo);
        Map<String, Object> result = new HashMap<>();
        try {
            List<FenzhangLog> logs = fenzhangLogMapper.findByOrderNo(orderNo);
            result.put("status", "success");
            result.put("count", logs.size());
            result.put("data", logs);
            logger.debug("获取到订单日志数量: {}", logs.size());
        } catch (Exception e) {
            logger.error("获取订单日志失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/status/summary")
    public Map<String, Object> getStatusSummary() {
        logger.debug("获取系统状态摘要");
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> pendingOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_PENDING);
            List<TransactionOrder> successOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_SUCCESS);
            List<TransactionOrder> failedOrders = orderMapper.findByProfitStatus(TransactionOrder.PROFIT_STATUS_FAILED);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("pendingCount", pendingOrders.size());
            summary.put("successCount", successOrders.size());
            summary.put("failedCount", failedOrders.size());
            summary.put("totalCount", pendingOrders.size() + successOrders.size() + failedOrders.size());
            
            Map<String, String> statusDesc = new HashMap<>();
            statusDesc.put("0", "待处理");
            statusDesc.put("1", "分润成功");
            statusDesc.put("2", "分润失败");
            summary.put("statusDescription", statusDesc);
            
            summary.put("systemVersion", "2.0.0");
            summary.put("calculationMode", "Database Stored Procedure");

            result.put("status", "success");
            result.put("data", summary);
            logger.debug("系统状态摘要: {}", summary);
        } catch (Exception e) {
            logger.error("获取系统状态摘要失败", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
}
