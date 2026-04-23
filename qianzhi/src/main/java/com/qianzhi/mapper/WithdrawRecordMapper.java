package com.qianzhi.mapper;

import com.qianzhi.entity.WithdrawRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WithdrawRecordMapper {
    
    WithdrawRecord selectById(@Param("id") Long id);
    
    List<WithdrawRecord> selectByAgentId(@Param("agentId") Long agentId);
    
    List<WithdrawRecord> selectList(@Param("agentId") Long agentId,
                                     @Param("status") Integer status,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime);
    
    int insert(WithdrawRecord withdrawRecord);
    
    int update(WithdrawRecord withdrawRecord);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
