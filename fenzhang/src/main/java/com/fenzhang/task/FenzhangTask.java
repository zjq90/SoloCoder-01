package com.fenzhang.task;

import com.fenzhang.service.ProfitService;
import com.fenzhang.service.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class FenzhangTask {

    private static final Logger logger = LoggerFactory.getLogger(FenzhangTask.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Autowired
    private ProfitService profitService;

    @Autowired
    private StatementService statementService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyProfitTask() {
        logger.info("========== 开始执行每日分润任务 ==========");
        try {
            profitService.processProfitBatch();
            logger.info("========== 每日分润任务执行完成 ==========");
        } catch (Exception e) {
            logger.error("每日分润任务执行失败", e);
        }
    }

    @Scheduled(cron = "0 30 2 * * ?")
    public void dailyStatementTask() {
        logger.info("========== 开始执行每日报表任务 ==========");
        try {
            String yesterday = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
            statementService.generateDailyStatement(yesterday);
            logger.info("========== 每日报表任务执行完成，日期: {} ==========", yesterday);
        } catch (Exception e) {
            logger.error("每日报表任务执行失败", e);
        }
    }

    @Scheduled(cron = "0 0 4 1 * ?")
    public void monthlyStatementTask() {
        logger.info("========== 开始执行月度报表任务 ==========");
        try {
            String lastMonth = LocalDate.now().minusMonths(1).format(MONTH_FORMATTER);
            statementService.generateMonthlyStatement(lastMonth);
            logger.info("========== 月度报表任务执行完成，月份: {} ==========", lastMonth);
        } catch (Exception e) {
            logger.error("月度报表任务执行失败", e);
        }
    }
}
