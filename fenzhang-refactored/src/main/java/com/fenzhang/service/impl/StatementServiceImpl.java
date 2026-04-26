package com.fenzhang.service.impl;

import com.fenzhang.entity.*;
import com.fenzhang.mapper.*;
import com.fenzhang.service.AgentService;
import com.fenzhang.service.StatementService;
import com.fenzhang.util.NoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final String PERIOD_PREFIX_DAILY = "D_";
    private static final String PERIOD_PREFIX_MONTHLY = "M_";

    @Resource
    private StatementMapper statementMapper;

    @Resource
    private TransactionOrderMapper orderMapper;

    @Resource
    private ProfitDetailMapper profitDetailMapper;

    @Resource
    private AgentService agentService;

    @Override
    public Statement findById(Long id) {
        logger.debug("根据ID查询对账单: {}", id);
        return statementMapper.findById(id);
    }

    @Override
    public Statement findByStatementNo(String statementNo) {
        logger.debug("根据编号查询对账单: {}", statementNo);
        return statementMapper.findByStatementNo(statementNo);
    }

    @Override
    public List<Statement> findByAgentCode(String agentCode) {
        logger.debug("根据代理商查询对账单: {}", agentCode);
        return statementMapper.findByAgentCode(agentCode);
    }

    @Override
    public List<Statement> findByStatementPeriod(String statementPeriod) {
        logger.debug("根据期间查询对账单: {}", statementPeriod);
        return statementMapper.findByStatementPeriod(statementPeriod);
    }

    @Override
    public List<Statement> findAll() {
        logger.debug("查询所有对账单");
        return statementMapper.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(Statement statement) {
        logger.info("保存对账单: {}", statement.getStatementNo());
        return statementMapper.insert(statement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Statement statement) {
        logger.info("更新对账单: {}", statement.getStatementNo());
        return statementMapper.update(statement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        logger.info("删除对账单: {}", id);
        return statementMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateDailyStatement(String date) {
        logger.info("========== 开始生成日报表，日期: {} ==========", date);
        
        List<Agent> allAgents = agentService.findEnabledAgents();
        logger.info("需要生成日报表的代理商数量: {}", allAgents.size());
        
        for (Agent agent : allAgents) {
            try {
                generateAgentDailyStatement(agent, date);
            } catch (Exception e) {
                logger.error("生成代理商[{}]日报表失败", agent.getAgentCode(), e);
            }
        }
        
        logger.info("========== 日报表生成完成，日期: {} ==========", date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateMonthlyStatement(String month) {
        logger.info("========== 开始生成月报表，月份: {} ==========", month);
        
        List<Agent> allAgents = agentService.findEnabledAgents();
        logger.info("需要生成月报表的代理商数量: {}", allAgents.size());
        
        for (Agent agent : allAgents) {
            try {
                generateAgentMonthlyStatement(agent, month);
            } catch (Exception e) {
                logger.error("生成代理商[{}]月报表失败", agent.getAgentCode(), e);
            }
        }
        
        logger.info("========== 月报表生成完成，月份: {} ==========", month);
    }

    private void generateAgentDailyStatement(Agent agent, String date) {
        String period = PERIOD_PREFIX_DAILY + date;
        
        List<Statement> existing = statementMapper.findByAgentCodeAndPeriod(agent.getAgentCode(), period);
        if (!existing.isEmpty()) {
            logger.warn("代理商[{}]日报表已存在，期间: {}", agent.getAgentCode(), period);
            return;
        }

        List<TransactionOrder> agentOrders = orderMapper.findByAgentCode(agent.getAgentCode());
        
        BigDecimal totalTransactionAmount = BigDecimal.ZERO;
        int transactionCount = 0;
        
        LocalDate targetDate = LocalDate.parse(date, DATE_FORMATTER);
        
        for (TransactionOrder order : agentOrders) {
            if (order.getTransactionTime() != null) {
                LocalDate orderDate = order.getTransactionTime().toLocalDate();
                if (targetDate.equals(orderDate)) {
                    totalTransactionAmount = totalTransactionAmount.add(order.getTransactionAmount());
                    transactionCount++;
                }
            }
        }

        List<ProfitDetail> profitDetails = profitDetailMapper.findByAgentCode(agent.getAgentCode());
        
        BigDecimal totalProfitAmount = BigDecimal.ZERO;
        int profitCount = 0;
        
        for (ProfitDetail detail : profitDetails) {
            if (detail.getProfitTime() != null) {
                LocalDate profitDate = detail.getProfitTime().toLocalDate();
                if (targetDate.equals(profitDate)) {
                    totalProfitAmount = totalProfitAmount.add(detail.getProfitAmount());
                    profitCount++;
                }
            }
        }

        Statement statement = new Statement();
        statement.setStatementNo(NoGenerator.generateStatementNo());
        statement.setStatementPeriod(period);
        statement.setAgentCode(agent.getAgentCode());
        statement.setAgentName(agent.getAgentName());
        statement.setTotalTransactionAmount(totalTransactionAmount);
        statement.setTotalProfitAmount(totalProfitAmount);
        statement.setTransactionCount(transactionCount);
        statement.setProfitCount(profitCount);
        statement.setStatementStatus(Statement.STATUS_CONFIRMED);
        
        statementMapper.insert(statement);
        
        logger.info("代理商[{}]日报表已生成，期间: {}，交易金额: {}，分润金额: {}，交易笔数: {}，分润笔数: {}", 
                agent.getAgentCode(), period, totalTransactionAmount, totalProfitAmount, 
                transactionCount, profitCount);
    }

    private void generateAgentMonthlyStatement(Agent agent, String month) {
        String period = PERIOD_PREFIX_MONTHLY + month;
        
        List<Statement> existing = statementMapper.findByAgentCodeAndPeriod(agent.getAgentCode(), period);
        if (!existing.isEmpty()) {
            logger.warn("代理商[{}]月报表已存在，期间: {}", agent.getAgentCode(), period);
            return;
        }

        List<TransactionOrder> agentOrders = orderMapper.findByAgentCode(agent.getAgentCode());
        
        BigDecimal totalTransactionAmount = BigDecimal.ZERO;
        int transactionCount = 0;
        
        int targetYear = Integer.parseInt(month.substring(0, 4));
        int targetMonth = Integer.parseInt(month.substring(5, 7));
        
        for (TransactionOrder order : agentOrders) {
            if (order.getTransactionTime() != null) {
                LocalDateTime orderTime = order.getTransactionTime();
                if (orderTime.getYear() == targetYear && orderTime.getMonthValue() == targetMonth) {
                    totalTransactionAmount = totalTransactionAmount.add(order.getTransactionAmount());
                    transactionCount++;
                }
            }
        }

        List<ProfitDetail> profitDetails = profitDetailMapper.findByAgentCode(agent.getAgentCode());
        
        BigDecimal totalProfitAmount = BigDecimal.ZERO;
        int profitCount = 0;
        
        for (ProfitDetail detail : profitDetails) {
            if (detail.getProfitTime() != null) {
                LocalDateTime profitTime = detail.getProfitTime();
                if (profitTime.getYear() == targetYear && profitTime.getMonthValue() == targetMonth) {
                    totalProfitAmount = totalProfitAmount.add(detail.getProfitAmount());
                    profitCount++;
                }
            }
        }

        Statement statement = new Statement();
        statement.setStatementNo(NoGenerator.generateStatementNo());
        statement.setStatementPeriod(period);
        statement.setAgentCode(agent.getAgentCode());
        statement.setAgentName(agent.getAgentName());
        statement.setTotalTransactionAmount(totalTransactionAmount);
        statement.setTotalProfitAmount(totalProfitAmount);
        statement.setTransactionCount(transactionCount);
        statement.setProfitCount(profitCount);
        statement.setStatementStatus(Statement.STATUS_CONFIRMED);
        
        statementMapper.insert(statement);
        
        logger.info("代理商[{}]月报表已生成，期间: {}，交易金额: {}，分润金额: {}，交易笔数: {}，分润笔数: {}", 
                agent.getAgentCode(), period, totalTransactionAmount, totalProfitAmount, 
                transactionCount, profitCount);
    }
}
