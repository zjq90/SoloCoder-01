package com.fenzhang.task;

import com.fenzhang.dto.BatchProfitResult;
import com.fenzhang.service.ProfitService;
import com.fenzhang.service.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FenzhangTask {

    private static final Logger logger = LoggerFactory.getLogger(FenzhangTask.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${fenzhang.task.profit-cron:0 0 2 * * ?}")
    private String profitCron;

    @Value("${fenzhang.task.daily-statement-cron:0 30 2 * * ?}")
    private String dailyStatementCron;

    @Value("${fenzhang.task.monthly-statement-cron:0 0 4 1 * ?}")
    private String monthlyStatementCron;

    @Autowired
    private ProfitService profitService;

    @Autowired
    private StatementService statementService;

    @Scheduled(cron = "${fenzhang.task.profit-cron:0 0 2 * * ?}")
    public void dailyProfitTask() {
        logger.info("========== 开始执行每日分润任务 ==========");
        logger.info("任务启动时间: {}", LocalDateTime.now().format(TIME_FORMATTER));
        logger.info("分润计算模式: 数据库存储过程模式");
        
        long startTime = System.currentTimeMillis();
        
        try {
            int pendingCount = profitService.countPendingOrders();
            logger.info("待处理订单数量: {}", pendingCount);
            
            if (pendingCount == 0) {
                logger.info("没有待处理的订单，分润任务结束");
                return;
            }

            BatchProfitResult result = profitService.processProfitBatch();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            logger.info("========== 每日分润任务执行完成 ==========");
            logger.info("批次号: {}", result.getBatchNo());
            logger.info("成功数量: {}", result.getSuccessCount());
            logger.info("失败数量: {}", result.getFailCount());
            logger.info("任务耗时: {}ms", duration);
            
            if (result.getFailCount() > 0) {
                logger.warn("========== 注意: 有 {} 个订单分润计算失败，请检查日志 ==========", result.getFailCount());
            }

        } catch (Exception e) {
            logger.error("========== 每日分润任务执行失败 ==========", e);
            
            long endTime = System.currentTimeMillis();
            logger.info("任务耗时: {}ms", (endTime - startTime));
        }
    }

    @Scheduled(cron = "${fenzhang.task.daily-statement-cron:0 30 2 * * ?}")
    public void dailyStatementTask() {
        logger.info("========== 开始执行每日报表任务 ==========");
        logger.info("任务启动时间: {}", LocalDateTime.now().format(TIME_FORMATTER));
        
        long startTime = System.currentTimeMillis();
        
        try {
            String yesterday = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
            logger.info("生成日报表日期: {}", yesterday);
            
            statementService.generateDailyStatement(yesterday);
            
            long endTime = System.currentTimeMillis();
            
            logger.info("========== 每日报表任务执行完成 ==========");
            logger.info("报表日期: {}", yesterday);
            logger.info("任务耗时: {}ms", (endTime - startTime));

        } catch (Exception e) {
            logger.error("========== 每日报表任务执行失败 ==========", e);
            
            long endTime = System.currentTimeMillis();
            logger.info("任务耗时: {}ms", (endTime - startTime));
        }
    }

    @Scheduled(cron = "${fenzhang.task.monthly-statement-cron:0 0 4 1 * ?}")
    public void monthlyStatementTask() {
        logger.info("========== 开始执行月度报表任务 ==========");
        logger.info("任务启动时间: {}", LocalDateTime.now().format(TIME_FORMATTER));
        
        long startTime = System.currentTimeMillis();
        
        try {
            String lastMonth = LocalDate.now().minusMonths(1).format(MONTH_FORMATTER);
            logger.info("生成月报表月份: {}", lastMonth);
            
            statementService.generateMonthlyStatement(lastMonth);
            
            long endTime = System.currentTimeMillis();
            
            logger.info("========== 月度报表任务执行完成 ==========");
            logger.info("报表月份: {}", lastMonth);
            logger.info("任务耗时: {}ms", (endTime - startTime));

        } catch (Exception e) {
            logger.error("========== 月度报表任务执行失败 ==========", e);
            
            long endTime = System.currentTimeMillis();
            logger.info("任务耗时: {}ms", (endTime - startTime));
        }
    }

    public void triggerProfitTaskManually() {
        logger.info("========== 手动触发分润任务 ==========");
        dailyProfitTask();
    }

    public void triggerDailyStatementTaskManually(String date) {
        logger.info("========== 手动触发日报表任务，日期: {} ==========", date);
        
        long startTime = System.currentTimeMillis();
        
        try {
            statementService.generateDailyStatement(date);
            
            long endTime = System.currentTimeMillis();
            
            logger.info("========== 手动日报表任务执行完成 ==========");
            logger.info("任务耗时: {}ms", (endTime - startTime));

        } catch (Exception e) {
            logger.error("========== 手动日报表任务执行失败 ==========", e);
        }
    }

    public void triggerMonthlyStatementTaskManually(String month) {
        logger.info("========== 手动触发月报表任务，月份: {} ==========", month);
        
        long startTime = System.currentTimeMillis();
        
        try {
            statementService.generateMonthlyStatement(month);
            
            long endTime = System.currentTimeMillis();
            
            logger.info("========== 手动月报表任务执行完成 ==========");
            logger.info("任务耗时: {}ms", (endTime - startTime));

        } catch (Exception e) {
            logger.error("========== 手动月报表任务执行失败 ==========", e);
        }
    }

    public String getTaskStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("分账系统定时任务配置:\n");
        sb.append("  - 每日分润任务 Cron: ").append(profitCron).append("\n");
        sb.append("  - 每日报表任务 Cron: ").append(dailyStatementCron).append("\n");
        sb.append("  - 月度报表任务 Cron: ").append(monthlyStatementCron).append("\n");
        sb.append("  - 当前时间: ").append(LocalDateTime.now().format(TIME_FORMATTER)).append("\n");
        sb.append("  - 分润计算模式: 数据库存储过程模式");
        
        return sb.toString();
    }
}
