package com.fenzhang.service.impl;

import com.fenzhang.dto.BatchProfitResult;
import com.fenzhang.dto.ProfitCalculateResult;
import com.fenzhang.entity.Agent;
import com.fenzhang.entity.FenzhangLog;
import com.fenzhang.mapper.*;
import com.fenzhang.service.ProfitService;
import com.fenzhang.util.NoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ProfitServiceImpl implements ProfitService {

    private static final Logger logger = LoggerFactory.getLogger(ProfitServiceImpl.class);

    private static final String PLATFORM_AGENT_CODE = "PLATFORM";
    private static final String REDIS_LOCK_KEY = "fenzhang:profit:lock";
    private static final long LOCK_EXPIRE_SECONDS = 300;

    @Value("${fenzhang.batch.size:100}")
    private int batchSize;

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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProfitCalculateResult calculateProfitForOrder(String orderNo, String batchNo) {
        logger.info("========== 开始调用存储过程计算订单[{}]分润，批次号: {} ==========", orderNo, batchNo);
        
        ProfitCalculateResult result = new ProfitCalculateResult();
        result.setOrderNo(orderNo);
        result.setBatchNo(batchNo);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("orderNo", orderNo);
            params.put("batchNo", batchNo);
            params.put("platformCode", PLATFORM_AGENT_CODE);
            params.put("resultCode", null);
            params.put("resultMsg", null);

            logger.debug("调用存储过程 sp_calculate_profit_for_order，参数: {}", params);
            orderMapper.callCalculateProfitForOrder(params);

            Integer resultCode = (Integer) params.get("resultCode");
            String resultMsg = (String) params.get("resultMsg");

            result.setResultCode(resultCode);
            result.setResultMsg(resultMsg);

            if (result.isSuccess()) {
                logger.info("========== 订单[{}]分润计算成功，结果: {} ==========", orderNo, resultMsg);
            } else {
                logger.warn("========== 订单[{}]分润计算失败，结果码: {}, 消息: {} ==========", orderNo, resultCode, resultMsg);
            }

        } catch (Exception e) {
            logger.error("========== 订单[{}]分润计算异常 ==========", orderNo, e);
            result.setResultCode(1);
            result.setResultMsg("系统异常: " + e.getMessage());

            saveLog(batchNo, orderNo, null, FenzhangLog.OP_CALCULATE_PROFIT, 
                    FenzhangLog.LEVEL_ERROR, "订单分润计算异常", getStackTraceAsString(e));
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchProfitResult calculateProfitForBatch(String batchNo) {
        logger.info("========== 开始调用存储过程批量计算分润，批次号: {} ==========", batchNo);
        
        BatchProfitResult result = new BatchProfitResult();
        result.setBatchNo(batchNo);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("batchNo", batchNo);
            params.put("platformCode", PLATFORM_AGENT_CODE);
            params.put("batchSize", batchSize);
            params.put("successCount", null);
            params.put("failCount", null);

            logger.debug("调用存储过程 sp_calculate_profit_batch，参数: {}", params);
            orderMapper.callCalculateProfitBatch(params);

            Integer successCount = (Integer) params.get("successCount");
            Integer failCount = (Integer) params.get("failCount");

            result.setSuccessCount(successCount != null ? successCount : 0);
            result.setFailCount(failCount != null ? failCount : 0);
            result.setEndTime(System.currentTimeMillis());

            logger.info("========== 批量分润计算完成，批次号: {}，成功: {}，失败: {}，耗时: {}ms ==========", 
                    batchNo, result.getSuccessCount(), result.getFailCount(), result.getDuration());

        } catch (Exception e) {
            logger.error("========== 批量分润计算异常，批次号: {} ==========", batchNo, e);
            result.setSuccessCount(0);
            result.setFailCount(0);
            result.setEndTime(System.currentTimeMillis());

            saveLog(batchNo, null, null, FenzhangLog.OP_BATCH_START, 
                    FenzhangLog.LEVEL_ERROR, "批量分润计算异常", getStackTraceAsString(e));
        }

        return result;
    }

    @Override
    public BatchProfitResult processProfitBatch() {
        String batchNo = NoGenerator.generateBatchNo();
        logger.info("========== 启动分润批次处理，批次号: {} ==========", batchNo);

        String lockValue = String.valueOf(System.currentTimeMillis());
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(REDIS_LOCK_KEY, lockValue, LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (locked == null || !locked) {
            logger.warn("========== 获取分布式锁失败，可能有其他任务正在运行 ==========");
            BatchProfitResult result = new BatchProfitResult();
            result.setBatchNo(batchNo);
            result.setSuccessCount(0);
            result.setFailCount(0);
            result.setEndTime(System.currentTimeMillis());
            return result;
        }

        try {
            int pendingCount = countPendingOrders();
            logger.info("待处理订单数量: {}", pendingCount);

            if (pendingCount == 0) {
                logger.info("没有待处理的订单，批次结束");
                BatchProfitResult result = new BatchProfitResult();
                result.setBatchNo(batchNo);
                result.setSuccessCount(0);
                result.setFailCount(0);
                result.setEndTime(System.currentTimeMillis());
                return result;
            }

            return calculateProfitForBatch(batchNo);

        } finally {
            String currentLockValue = (String) redisTemplate.opsForValue().get(REDIS_LOCK_KEY);
            if (lockValue.equals(currentLockValue)) {
                redisTemplate.delete(REDIS_LOCK_KEY);
                logger.debug("释放分布式锁");
            }
        }
    }

    @Override
    public int countPendingOrders() {
        return orderMapper.countPendingOrders();
    }

    @Override
    public String getPlatformAgentCode() {
        return PLATFORM_AGENT_CODE;
    }

    private void saveLog(String batchNo, String orderNo, String agentCode, 
                         String operationType, String logLevel, String message, String detail) {
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
            logger.debug("保存日志: operationType={}, message={}", operationType, message);
        } catch (Exception e) {
            logger.error("保存日志失败: operationType={}, message={}", operationType, message, e);
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
