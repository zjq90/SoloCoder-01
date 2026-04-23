package com.fenzhang.service.impl;

import com.alibaba.fastjson.JSON;
import com.fenzhang.entity.*;
import com.fenzhang.mapper.*;
import com.fenzhang.service.*;
import com.fenzhang.util.NoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfitServiceImpl implements ProfitService {

    private static final Logger logger = LoggerFactory.getLogger(ProfitServiceImpl.class);

    public static final int ORDER_PROFIT_STATUS_PENDING = 0;
    public static final int ORDER_PROFIT_STATUS_SUCCESS = 1;
    public static final int ORDER_PROFIT_STATUS_FAILED = 2;

    @Resource
    private TransactionOrderMapper orderMapper;

    @Resource
    private AgentMapper agentMapper;

    @Resource
    private ProfitDetailMapper profitDetailMapper;

    @Resource
    private AgentAccountMapper agentAccountMapper;

    @Resource
    private AccountDetailMapper accountDetailMapper;

    @Resource
    private FenzhangLogMapper fenzhangLogMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void calculateProfitForOrder(TransactionOrder order, String batchNo) {
        logger.info("开始计算订单[{}]的分润，代理商:[{}]", order.getOrderNo(), order.getAgentCode());
        
        String logMsg = "开始处理订单: " + order.getOrderNo();
        saveLog(batchNo, order.getOrderNo(), null, "CALCULATE_PROFIT", "INFO", logMsg, JSON.toJSONString(order));

        try {
            Agent currentAgent = agentMapper.findByAgentCode(order.getAgentCode());
            if (currentAgent == null) {
                String errorMsg = "代理商不存在: " + order.getAgentCode();
                saveLog(batchNo, order.getOrderNo(), order.getAgentCode(), "CALCULATE_PROFIT", "ERROR", errorMsg, null);
                logger.error(errorMsg);
                orderMapper.updateProfitStatus(order.getOrderNo(), ORDER_PROFIT_STATUS_FAILED);
                saveLog(batchNo, order.getOrderNo(), null, "ORDER_FAILED", "ERROR", 
                        "订单已标记为失败状态: " + errorMsg, null);
                return;
            }

            Agent platform = agentMapper.findByAgentCode("PLATFORM");
            if (platform == null) {
                String errorMsg = "平台配置不存在";
                saveLog(batchNo, order.getOrderNo(), null, "CALCULATE_PROFIT", "ERROR", errorMsg, null);
                logger.error(errorMsg);
                orderMapper.updateProfitStatus(order.getOrderNo(), ORDER_PROFIT_STATUS_FAILED);
                saveLog(batchNo, order.getOrderNo(), null, "ORDER_FAILED", "ERROR", 
                        "订单已标记为失败状态: " + errorMsg, null);
                return;
            }

            List<Agent> agentChain = buildAgentChain(currentAgent, platform);
            logger.info("订单[{}]的代理链: {}", order.getOrderNo(), agentChain);

            for (int i = 0; i < agentChain.size(); i++) {
                Agent current = agentChain.get(i);
                Agent parent = (i == agentChain.size() - 1) ? platform : agentChain.get(i + 1);

                BigDecimal parentRate = parent.getProfitRate();
                BigDecimal currentRate = current.getProfitRate();
                BigDecimal rateDiff = parentRate.subtract(currentRate);

                if (rateDiff.compareTo(BigDecimal.ZERO) <= 0) {
                    String warnMsg = "分润点差非正，跳过代理商: " + current.getAgentCode();
                    saveLog(batchNo, order.getOrderNo(), current.getAgentCode(), "CALCULATE_PROFIT", "WARN", warnMsg, 
                            "parentRate=" + parentRate + ", currentRate=" + currentRate);
                    logger.warn(warnMsg);
                    continue;
                }

                BigDecimal profitAmount = rateDiff.multiply(order.getTransactionAmount())
                        .setScale(2, RoundingMode.HALF_UP);

                ProfitDetail profitDetail = createProfitDetail(order, current, parent, platform, rateDiff, profitAmount);
                profitDetailMapper.insert(profitDetail);

                updateAgentAccount(current, profitAmount, order.getOrderNo(), profitDetail.getProfitNo(), batchNo);

                String successMsg = "代理商[" + current.getAgentCode() + "]分润计算成功，金额: " + profitAmount;
                saveLog(batchNo, order.getOrderNo(), current.getAgentCode(), "CALCULATE_PROFIT", "INFO", successMsg,
                        "profitNo=" + profitDetail.getProfitNo() + ", rateDiff=" + rateDiff + ", profitAmount=" + profitAmount);
                logger.info(successMsg);
            }

            orderMapper.updateProfitStatus(order.getOrderNo(), ORDER_PROFIT_STATUS_SUCCESS);

            String completeMsg = "订单[" + order.getOrderNo() + "]分润计算完成";
            saveLog(batchNo, order.getOrderNo(), null, "CALCULATE_PROFIT", "INFO", completeMsg, null);
            logger.info(completeMsg);

        } catch (Exception e) {
            String errorMsg = "订单[" + order.getOrderNo() + "]分润计算失败: " + e.getMessage();
            String stackTrace = getStackTraceAsString(e);
            
            saveLog(batchNo, order.getOrderNo(), null, "CALCULATE_PROFIT", "ERROR", errorMsg, stackTrace);
            logger.error(errorMsg, e);
            
            try {
                orderMapper.updateProfitStatus(order.getOrderNo(), ORDER_PROFIT_STATUS_FAILED);
                saveLog(batchNo, order.getOrderNo(), null, "ORDER_FAILED", "ERROR", 
                        "订单已标记为失败状态，需人工处理", stackTrace);
            } catch (Exception ex) {
                logger.error("更新订单状态失败: {}", order.getOrderNo(), ex);
                saveLog(batchNo, order.getOrderNo(), null, "STATUS_UPDATE_FAILED", "ERROR", 
                        "更新订单状态失败: " + ex.getMessage(), getStackTraceAsString(ex));
            }
            
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    public void calculateProfitForOrders(List<TransactionOrder> orders, String batchNo) {
        logger.info("开始批量计算分润，订单数量: {}, 批次号: {}", orders.size(), batchNo);
        
        String batchStartMsg = "批次开始处理，订单数量: " + orders.size();
        saveLog(batchNo, null, null, "BATCH_START", "INFO", batchStartMsg, null);
        logger.info(batchStartMsg);

        int successCount = 0;
        int failCount = 0;
        List<String> failedOrders = new ArrayList<>();

        for (TransactionOrder order : orders) {
            try {
                calculateProfitForOrder(order, batchNo);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedOrders.add(order.getOrderNo());
                logger.error("订单处理失败，继续处理下一个订单: {}", order.getOrderNo(), e);
                
                saveLog(batchNo, order.getOrderNo(), null, "ORDER_SKIP", "WARN", 
                        "订单处理异常，已跳过，继续处理其他订单: " + e.getMessage(), null);
            }
        }

        String batchEndMsg = "批次处理完成，成功: " + successCount + ", 失败: " + failCount;
        if (!failedOrders.isEmpty()) {
            batchEndMsg += "，失败订单: " + String.join(", ", failedOrders);
        }
        
        saveLog(batchNo, null, null, "BATCH_END", "INFO", batchEndMsg, 
                "successCount=" + successCount + ", failCount=" + failCount + 
                ", failedOrders=" + JSON.toJSONString(failedOrders));
        logger.info(batchEndMsg);
    }

    @Override
    public void processProfitBatch() {
        String batchNo = NoGenerator.generateBatchNo();
        logger.info("启动分润批次处理，批次号: {}", batchNo);

        List<TransactionOrder> pendingOrders = orderMapper.findByProfitStatus(ORDER_PROFIT_STATUS_PENDING);
        logger.info("待处理订单数量: {}", pendingOrders.size());

        if (pendingOrders.isEmpty()) {
            logger.info("没有待处理的订单，批次结束");
            return;
        }

        calculateProfitForOrders(pendingOrders, batchNo);
    }

    private List<Agent> buildAgentChain(Agent startAgent, Agent platform) {
        List<Agent> chain = new ArrayList<>();
        Agent current = startAgent;

        while (current != null && !"PLATFORM".equals(current.getAgentCode())) {
            chain.add(current);
            if (current.getParentAgentCode() == null || "PLATFORM".equals(current.getParentAgentCode())) {
                break;
            }
            current = agentMapper.findByAgentCode(current.getParentAgentCode());
        }

        return chain;
    }

    private ProfitDetail createProfitDetail(TransactionOrder order, Agent currentAgent, Agent parentAgent, 
                                             Agent platform, BigDecimal rateDiff, BigDecimal profitAmount) {
        ProfitDetail detail = new ProfitDetail();
        detail.setProfitNo(NoGenerator.generateProfitNo());
        detail.setOrderNo(order.getOrderNo());
        detail.setAgentCode(currentAgent.getAgentCode());
        detail.setAgentName(currentAgent.getAgentName());
        detail.setParentAgentCode(parentAgent.getAgentCode());
        detail.setTransactionAmount(order.getTransactionAmount());
        detail.setPlatformProfitRate(platform.getProfitRate());
        detail.setAgentProfitRate(currentAgent.getProfitRate());
        detail.setProfitRateDiff(rateDiff);
        detail.setProfitAmount(profitAmount);
        detail.setProfitTime(LocalDateTime.now());
        return detail;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    protected void updateAgentAccount(Agent agent, BigDecimal amount, String orderNo, String profitNo, String batchNo) {
        AgentAccount account = agentAccountMapper.findByAgentCode(agent.getAgentCode());
        
        if (account == null) {
            account = new AgentAccount();
            account.setAgentCode(agent.getAgentCode());
            account.setAgentName(agent.getAgentName());
            account.setBalance(amount);
            account.setTotalProfit(amount);
            account.setWithdrawAmount(BigDecimal.ZERO);
            account.setFrozenAmount(BigDecimal.ZERO);
            account.setStatus(1);
            agentAccountMapper.insert(account);
        } else {
            BigDecimal beforeBalance = account.getBalance();
            BigDecimal afterBalance = beforeBalance.add(amount);
            BigDecimal newTotalProfit = account.getTotalProfit().add(amount);
            
            agentAccountMapper.updateBalance(agent.getAgentCode(), afterBalance, newTotalProfit);
        }

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setDetailNo(NoGenerator.generateDetailNo());
        accountDetail.setAgentCode(agent.getAgentCode());
        accountDetail.setAgentName(agent.getAgentName());
        accountDetail.setOrderNo(orderNo);
        accountDetail.setProfitNo(profitNo);
        accountDetail.setTransactionType("PROFIT_IN");
        accountDetail.setAmount(amount);
        accountDetail.setBeforeBalance(account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO);
        accountDetail.setAfterBalance(
            account.getBalance() != null ? account.getBalance().add(amount) : amount
        );
        accountDetail.setRemark("分润入账");
        accountDetailMapper.insert(accountDetail);

        String logMsg = "账户更新成功，代理商: " + agent.getAgentCode() + ", 金额: " + amount;
        saveLog(batchNo, orderNo, agent.getAgentCode(), "ACCOUNT_UPDATE", "INFO", logMsg, 
                "profitNo=" + profitNo + ", amount=" + amount);
    }

    private void saveLog(String batchNo, String orderNo, String agentCode, String operationType, 
                         String logLevel, String message, String detail) {
        try {
            FenzhangLog log = new FenzhangLog();
            log.setLogNo(NoGenerator.generateLogNo());
            log.setBatchNo(batchNo);
            log.setOrderNo(orderNo);
            log.setAgentCode(agentCode);
            log.setOperationType(operationType);
            log.setLogLevel(logLevel);
            log.setMessage(message);
            log.setDetail(detail);
            fenzhangLogMapper.insert(log);
        } catch (Exception e) {
            logger.error("保存日志失败: operationType={}, message={}", operationType, message, e);
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
