package com.qianzhi.service;

import com.qianzhi.entity.Product;
import com.qianzhi.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    public Product getByCode(String productCode) {
        return productMapper.selectByCode(productCode);
    }

    public List<Product> list() {
        return productMapper.selectAll();
    }

    public List<Product> list(String productName, Integer status) {
        return productMapper.selectList(productName, status);
    }

    @Transactional
    public void add(Product product) {
        Product existing = productMapper.selectByCode(product.getProductCode());
        if (existing != null) {
            throw new IllegalArgumentException("产品编码已存在");
        }
        productMapper.insert(product);
    }

    @Transactional
    public void update(Product product) {
        Product existing = productMapper.selectById(product.getId());
        if (existing == null) {
            throw new IllegalArgumentException("产品不存在");
        }
        productMapper.update(product);
    }

    @Transactional
    public void delete(Long id) {
        productMapper.deleteById(id);
    }
}
