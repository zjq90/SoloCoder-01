package com.fenzhang.service;

import com.fenzhang.entity.Statement;

import java.util.List;

public interface StatementService {

    Statement findById(Long id);

    Statement findByStatementNo(String statementNo);

    List<Statement> findByAgentCode(String agentCode);

    List<Statement> findByStatementPeriod(String statementPeriod);

    List<Statement> findAll();

    int save(Statement statement);

    int update(Statement statement);

    int deleteById(Long id);

    void generateDailyStatement(String date);

    void generateMonthlyStatement(String month);
}
