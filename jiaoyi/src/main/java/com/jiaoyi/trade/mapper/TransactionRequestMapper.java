package com.jiaoyi.trade.mapper;

import com.jiaoyi.trade.entity.TransactionRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TransactionRequestMapper {

    @Select("SELECT * FROM transaction_request WHERE uuid = #{uuid}")
    TransactionRequest selectByUuid(@Param("uuid") String uuid);

    @Select("SELECT * FROM transaction_request WHERE order_no = #{orderNo}")
    TransactionRequest selectByOrderNo(@Param("orderNo") String orderNo);

    @Select("SELECT * FROM transaction_request WHERE id = #{id}")
    TransactionRequest selectById(@Param("id") Long id);

    int insert(TransactionRequest record);

    int updateById(TransactionRequest record);

    List<TransactionRequest> selectByClientNo(@Param("clientNo") String clientNo);
}
