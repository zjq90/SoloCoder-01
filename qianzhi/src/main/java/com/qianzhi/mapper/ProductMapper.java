package com.qianzhi.mapper;

import com.qianzhi.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    
    Product selectById(@Param("id") Long id);
    
    Product selectByCode(@Param("productCode") String productCode);
    
    List<Product> selectAll();
    
    List<Product> selectList(@Param("productName") String productName,
                              @Param("status") Integer status);
    
    int insert(Product product);
    
    int update(Product product);
    
    int deleteById(@Param("id") Long id);
}
