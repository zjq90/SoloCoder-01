package com.fenzhang.mapper;

import com.fenzhang.entity.Statement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatementMapper {

    Statement findById(@Param("id") Long id);

    Statement findByStatementNo(@Param("statementNo") String statementNo);

    List<Statement> findByAgentCode(@Param("agentCode") String agentCode);

    List<Statement> findByStatementPeriod(@Param("statementPeriod") String statementPeriod);

    List<Statement> findByAgentCodeAndPeriod(@Param("agentCode") String agentCode, 
                                               @Param("statementPeriod") String statementPeriod);

    List<Statement> findAll();

    int insert(Statement statement);

    int update(Statement statement);

    int deleteById(@Param("id") Long id);
}
