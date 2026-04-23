package com.fenzhang.controller;

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
import com.fenzhang.service.impl.ProfitServiceImpl;

@RestController
@RequestMapping("/api/fenzhang")
public class FenzhangController {

    private static final Logger logger = LoggerFactory.getLogger(FenzhangController.class);

    @Autowired
    private ProfitService profitService;

    @Autowired
    private TransactionOrderService orderService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentAccountService accountService;

    @Autowired
    private StatementService statementService;

    @Resource
    private FenzhangLogMapper fenzhangLogMapper;

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "分账系统运行正常");
        result.put("time", LocalDate.now().toString());
        return result;
    }

    @PostMapping("/calculate")
    public Map<String, Object> calculateProfit() {
        Map<String, Object> result = new HashMap<>();
        try {
            String batchNo = NoGenerator.generateBatchNo();
            logger.info("手动触发分润计算，批次号: {}", batchNo);
            
            List<TransactionOrder> pendingOrders = orderService.findByProfitStatus(0);
            int orderCount = pendingOrders.size();
            
            if (orderCount == 0) {
                result.put("status", "success");
                result.put("message", "没有待计算分润的订单");
                result.put("orderCount", 0);
                return result;
            }
            
            profitService.calculateProfitForOrders(pendingOrders, batchNo);
            
            result.put("status", "success");
            result.put("message", "分润计算完成");
            result.put("batchNo", batchNo);
            result.put("orderCount", orderCount);
            
        } catch (Exception e) {
            logger.error("手动分润计算失败", e);
            result.put("status", "error");
            result.put("message", "分润计算失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/agents")
    public Map<String, Object> getAgents() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Agent> agents = agentService.findAllAgents();
            result.put("status", "success");
            result.put("data", agents);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders")
    public Map<String, Object> getOrders(@RequestParam(required = false) Integer profitStatus) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> orders;
            if (profitStatus != null) {
                orders = orderService.findByProfitStatus(profitStatus);
            } else {
                orders = orderService.findAll();
            }
            result.put("status", "success");
            result.put("data", orders);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/accounts")
    public Map<String, Object> getAccounts() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<AgentAccount> accounts = accountService.findAll();
            result.put("status", "success");
            result.put("data", accounts);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/statement/daily")
    public Map<String, Object> generateDailyStatement(@RequestParam(required = false) String date) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (date == null) {
                date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            statementService.generateDailyStatement(date);
            result.put("status", "success");
            result.put("message", "日报表生成完成");
            result.put("date", date);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/statements")
    public Map<String, Object> getStatements(@RequestParam(required = false) String period) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Statement> statements;
            if (period != null) {
                statements = statementService.findByStatementPeriod(period);
            } else {
                statements = statementService.findAll();
            }
            result.put("status", "success");
            result.put("data", statements);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders/failed")
    public Map<String, Object> getFailedOrders() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> failedOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_FAILED);
            result.put("status", "success");
            result.put("count", failedOrders.size());
            result.put("data", failedOrders);
            result.put("message", "订单状态说明: 0=待处理, 1=成功, 2=失败");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders/pending")
    public Map<String, Object> getPendingOrders() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> pendingOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_PENDING);
            result.put("status", "success");
            result.put("count", pendingOrders.size());
            result.put("data", pendingOrders);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/orders/success")
    public Map<String, Object> getSuccessOrders() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> successOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_SUCCESS);
            result.put("status", "success");
            result.put("count", successOrders.size());
            result.put("data", successOrders);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/order/retry/{orderNo}")
    public Map<String, Object> retryOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            TransactionOrder order = orderService.findByOrderNo(orderNo);
            if (order == null) {
                result.put("status", "error");
                result.put("message", "订单不存在: " + orderNo);
                return result;
            }

            if (order.getProfitStatus() != ProfitServiceImpl.ORDER_PROFIT_STATUS_FAILED) {
                result.put("status", "error");
                result.put("message", "订单状态不是失败状态，当前状态: " + order.getProfitStatus());
                return result;
            }

            orderService.updateProfitStatus(orderNo, ProfitServiceImpl.ORDER_PROFIT_STATUS_PENDING);
            
            String batchNo = NoGenerator.generateBatchNo();
            profitService.calculateProfitForOrder(order, batchNo);

            TransactionOrder updatedOrder = orderService.findByOrderNo(orderNo);
            result.put("status", "success");
            result.put("message", "订单重试完成");
            result.put("orderNo", orderNo);
            result.put("newStatus", updatedOrder.getProfitStatus());
            result.put("batchNo", batchNo);

        } catch (Exception e) {
            logger.error("订单重试失败: {}", orderNo, e);
            result.put("status", "error");
            result.put("message", "订单重试失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/orders/retry-all-failed")
    public Map<String, Object> retryAllFailedOrders() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> failedOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_FAILED);
            
            if (failedOrders.isEmpty()) {
                result.put("status", "success");
                result.put("message", "没有失败的订单需要重试");
                result.put("count", 0);
                return result;
            }

            for (TransactionOrder order : failedOrders) {
                orderService.updateProfitStatus(order.getOrderNo(), ProfitServiceImpl.ORDER_PROFIT_STATUS_PENDING);
            }

            String batchNo = NoGenerator.generateBatchNo();
            profitService.calculateProfitForOrders(failedOrders, batchNo);

            result.put("status", "success");
            result.put("message", "失败订单批量重试已触发");
            result.put("batchNo", batchNo);
            result.put("retryCount", failedOrders.size());

        } catch (Exception e) {
            logger.error("批量重试失败订单失败", e);
            result.put("status", "error");
            result.put("message", "批量重试失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/logs/batch/{batchNo}")
    public Map<String, Object> getLogsByBatchNo(@PathVariable String batchNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FenzhangLog> logs = fenzhangLogMapper.findByBatchNo(batchNo);
            result.put("status", "success");
            result.put("count", logs.size());
            result.put("data", logs);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/logs/order/{orderNo}")
    public Map<String, Object> getLogsByOrderNo(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FenzhangLog> logs = fenzhangLogMapper.findByOrderNo(orderNo);
            result.put("status", "success");
            result.put("count", logs.size());
            result.put("data", logs);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/status/summary")
    public Map<String, Object> getStatusSummary() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TransactionOrder> pendingOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_PENDING);
            List<TransactionOrder> successOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_SUCCESS);
            List<TransactionOrder> failedOrders = orderService.findByProfitStatus(ProfitServiceImpl.ORDER_PROFIT_STATUS_FAILED);
            
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

            result.put("status", "success");
            result.put("data", summary);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
}
