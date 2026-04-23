package com.jiaoyi.trade.mapper;

import com.jiaoyi.trade.entity.ChannelRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChannelRequestMapper {

    @Select("SELECT * FROM channel_request WHERE transaction_id = #{transactionId}")
    List<ChannelRequest> selectByTransactionId(@Param("transactionId") Long transactionId);

    @Select("SELECT * FROM channel_request WHERE channel_order_no = #{channelOrderNo}")
    ChannelRequest selectByChannelOrderNo(@Param("channelOrderNo") String channelOrderNo);

    @Select("SELECT * FROM channel_request WHERE id = #{id}")
    ChannelRequest selectById(@Param("id") Long id);

    int insert(ChannelRequest record);

    int updateById(ChannelRequest record);
}
