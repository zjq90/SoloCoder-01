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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

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
        return statementMapper.findById(id);
    }

    @Override
    public Statement findByStatementNo(String statementNo) {
        return statementMapper.findByStatementNo(statementNo);
    }

    @Override
    public List<Statement> findByAgentCode(String agentCode) {
        return statementMapper.findByAgentCode(agentCode);
    }

    @Override
    public List<Statement> findByStatementPeriod(String statementPeriod) {
        return statementMapper.findByStatementPeriod(statementPeriod);
    }

    @Override
    public List<Statement> findAll() {
        return statementMapper.findAll();
    }

    @Override
    public int save(Statement statement) {
        return statementMapper.insert(statement);
    }

    @Override
    public int update(Statement statement) {
        return statementMapper.update(statement);
    }

    @Override
    public int deleteById(Long id) {
        return statementMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateDailyStatement(String date) {
        logger.info("开始生成日报表，日期: {}", date);
        
        List<Agent> allAgents = agentService.findAllAgents();
        
        for (Agent agent : allAgents) {
            generateAgentDailyStatement(agent, date);
        }
        
        logger.info("日报表生成完成，日期: {}", date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateMonthlyStatement(String month) {
        logger.info("开始生成月报表，月份: {}", month);
        
        List<Agent> allAgents = agentService.findAllAgents();
        
        for (Agent agent : allAgents) {
            generateAgentMonthlyStatement(agent, month);
        }
        
        logger.info("月报表生成完成，月份: {}", month);
    }

    private void generateAgentDailyStatement(Agent agent, String date) {
        String period = "D_" + date;
        
        List<TransactionOrder> agentOrders = orderMapper.findByAgentCode(agent.getAgentCode());
        
        BigDecimal totalTransactionAmount = BigDecimal.ZERO;
        int transactionCount = 0;
        
        for (TransactionOrder order : agentOrders) {
            if (order.getTransactionTime() != null) {
                String orderDate = order.getTransactionTime().format(DATE_FORMATTER);
                if (date.equals(orderDate)) {
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
                String profitDate = detail.getProfitTime().format(DATE_FORMATTER);
                if (date.equals(profitDate)) {
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
        statement.setStatementStatus(1);
        
        statementMapper.insert(statement);
        
        logger.info("代理商[{}]日报表已生成，交易金额: {}, 分润金额: {}", 
                agent.getAgentCode(), totalTransactionAmount, totalProfitAmount);
    }

    private void generateAgentMonthlyStatement(Agent agent, String month) {
        String period = "M_" + month;
        
        List<TransactionOrder> agentOrders = orderMapper.findByAgentCode(agent.getAgentCode());
        
        BigDecimal totalTransactionAmount = BigDecimal.ZERO;
        int transactionCount = 0;
        
        for (TransactionOrder order : agentOrders) {
            if (order.getTransactionTime() != null) {
                String orderMonth = order.getTransactionTime().format(MONTH_FORMATTER);
                if (month.equals(orderMonth)) {
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
                String profitMonth = detail.getProfitTime().format(MONTH_FORMATTER);
                if (month.equals(profitMonth)) {
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
        statement.setStatementStatus(1);
        
        statementMapper.insert(statement);
        
        logger.info("代理商[{}]月报表已生成，交易金额: {}, 分润金额: {}", 
                agent.getAgentCode(), totalTransactionAmount, totalProfitAmount);
    }
}
