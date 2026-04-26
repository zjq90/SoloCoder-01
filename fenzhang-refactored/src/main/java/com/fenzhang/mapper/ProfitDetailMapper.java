package com.fenzhang.mapper;

import com.fenzhang.entity.ProfitDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfitDetailMapper {

    ProfitDetail findById(@Param("id") Long id);

    ProfitDetail findByProfitNo(@Param("profitNo") String profitNo);

    List<ProfitDetail> findByOrderNo(@Param("orderNo") String orderNo);

    List<ProfitDetail> findByAgentCode(@Param("agentCode") String agentCode);

    List<ProfitDetail> findAll();

    int insert(ProfitDetail detail);

    int update(ProfitDetail detail);

    int deleteById(@Param("id") Long id);
}
