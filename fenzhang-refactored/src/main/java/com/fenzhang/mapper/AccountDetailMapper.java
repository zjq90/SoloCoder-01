package com.fenzhang.mapper;

import com.fenzhang.entity.AccountDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountDetailMapper {

    AccountDetail findById(@Param("id") Long id);

    AccountDetail findByDetailNo(@Param("detailNo") String detailNo);

    List<AccountDetail> findByAgentCode(@Param("agentCode") String agentCode);

    List<AccountDetail> findByProfitNo(@Param("profitNo") String profitNo);

    List<AccountDetail> findByOrderNo(@Param("orderNo") String orderNo);

    List<AccountDetail> findAll();

    int insert(AccountDetail detail);

    int update(AccountDetail detail);

    int deleteById(@Param("id") Long id);
}
