package com.jiaoyi.trade.mapper;

import com.jiaoyi.trade.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TransactionRecordMapper {

    @Select("SELECT * FROM transaction_record WHERE uuid = #{uuid}")
    TransactionRecord selectByUuid(@Param("uuid") String uuid);

    @Select("SELECT * FROM transaction_record WHERE order_no = #{orderNo}")
    TransactionRecord selectByOrderNo(@Param("orderNo") String orderNo);

    @Select("SELECT * FROM transaction_record WHERE channel_order_no = #{channelOrderNo}")
    TransactionRecord selectByChannelOrderNo(@Param("channelOrderNo") String channelOrderNo);

    @Select("SELECT * FROM transaction_record WHERE id = #{id}")
    TransactionRecord selectById(@Param("id") Long id);

    int insert(TransactionRecord record);

    int updateById(TransactionRecord record);

    List<TransactionRecord> selectByClientNo(@Param("clientNo") String clientNo);

    List<TransactionRecord> selectByClientNoAndStatus(@Param("clientNo") String clientNo, @Param("status") Integer status);
}
