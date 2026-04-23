package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.entity.Product;
import com.qianzhi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("产品不存在");
        }
        return Result.success(product);
    }

    @GetMapping("/all")
    public Result<List<Product>> getAll() {
        List<Product> list = productService.list();
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result<List<Product>> list(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer status) {
        List<Product> list = productService.list(productName, status);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Product product) {
        productService.add(product);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Product product) {
        productService.update(product);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }
}
