package com.jiaoyi.trade.mapper;

import com.jiaoyi.trade.entity.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChannelMapper {

    @Select("SELECT * FROM channel WHERE status = 1 ORDER BY priority ASC")
    List<Channel> selectAllAvailable();

    @Select("SELECT * FROM channel WHERE channel_code = #{channelCode}")
    Channel selectByCode(@Param("channelCode") String channelCode);

    @Select("SELECT * FROM channel WHERE id = #{id}")
    Channel selectById(@Param("id") Long id);

    @Update("UPDATE channel SET current_transactions = current_transactions + 1, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int incrementTransactionCount(@Param("id") Long id);

    @Update("UPDATE channel SET current_transactions = 0, update_time = CURRENT_TIMESTAMP")
    int resetAllTransactionCount();

    int insert(Channel channel);

    int updateById(Channel channel);
}
