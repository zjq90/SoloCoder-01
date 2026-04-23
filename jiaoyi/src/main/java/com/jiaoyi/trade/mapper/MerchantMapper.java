package com.jiaoyi.trade.mapper;

import com.jiaoyi.trade.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MerchantMapper {

    @Select("SELECT * FROM merchant WHERE client_no = #{clientNo}")
    Merchant selectByClientNo(@Param("clientNo") String clientNo);

    @Select("SELECT * FROM merchant WHERE id = #{id}")
    Merchant selectById(@Param("id") Long id);

    @Update("UPDATE merchant SET balance = #{balance}, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int updateBalance(@Param("id") Long id, @Param("balance") java.math.BigDecimal balance);

    int insert(Merchant merchant);

    int updateById(Merchant merchant);
}
