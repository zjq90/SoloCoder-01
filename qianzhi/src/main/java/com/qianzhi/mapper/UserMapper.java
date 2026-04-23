package com.qianzhi.mapper;

import com.qianzhi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    User selectById(@Param("id") Long id);
    
    User selectByPhone(@Param("phone") String phone);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(@Param("id") Long id);
}
