package com.qianzhi.mapper;

import com.qianzhi.entity.AccountDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountDetailMapper {
    
    AccountDetail selectById(@Param("id") Long id);
    
    List<AccountDetail> selectByAgentId(@Param("agentId") Long agentId);
    
    List<AccountDetail> selectList(@Param("agentId") Long agentId,
                                    @Param("type") Integer type,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);
    
    long countByAgentId(@Param("agentId") Long agentId);
    
    List<AccountDetail> selectPage(@Param("agentId") Long agentId,
                                    @Param("offset") Long offset,
                                    @Param("size") Long size);
    
    int insert(AccountDetail accountDetail);
}
