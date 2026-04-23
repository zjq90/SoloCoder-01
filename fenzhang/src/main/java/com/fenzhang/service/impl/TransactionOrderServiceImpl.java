package com.fenzhang.service.impl;

import com.fenzhang.entity.TransactionOrder;
import com.fenzhang.mapper.TransactionOrderMapper;
import com.fenzhang.service.TransactionOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionOrderServiceImpl implements TransactionOrderService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionOrderServiceImpl.class);

    private static final String ORDER_CACHE_PREFIX = "order:";

    @Resource
    private TransactionOrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public TransactionOrder findById(Long id) {
        String cacheKey = ORDER_CACHE_PREFIX + "id:" + id;
        TransactionOrder order = (TransactionOrder) redisTemplate.opsForValue().get(cacheKey);
        if (order != null) {
            return order;
        }
        order = orderMapper.findById(id);
        if (order != null) {
            redisTemplate.opsForValue().set(cacheKey, order, 5, TimeUnit.MINUTES);
        }
        return order;
    }

    @Override
    public TransactionOrder findByOrderNo(String orderNo) {
        String cacheKey = ORDER_CACHE_PREFIX + "no:" + orderNo;
        TransactionOrder order = (TransactionOrder) redisTemplate.opsForValue().get(cacheKey);
        if (order != null) {
            return order;
        }
        order = orderMapper.findByOrderNo(orderNo);
        if (order != null) {
            redisTemplate.opsForValue().set(cacheKey, order, 5, TimeUnit.MINUTES);
        }
        return order;
    }

    @Override
    public List<TransactionOrder> findByProfitStatus(Integer profitStatus) {
        return orderMapper.findByProfitStatus(profitStatus);
    }

    @Override
    public List<TransactionOrder> findByAgentCode(String agentCode) {
        return orderMapper.findByAgentCode(agentCode);
    }

    @Override
    public List<TransactionOrder> findAll() {
        return orderMapper.findAll();
    }

    @Override
    public int save(TransactionOrder order) {
        clearOrderCache(order);
        return orderMapper.insert(order);
    }

    @Override
    public int update(TransactionOrder order) {
        clearOrderCache(order);
        return orderMapper.update(order);
    }

    @Override
    public int updateProfitStatus(String orderNo, Integer profitStatus) {
        TransactionOrder order = findByOrderNo(orderNo);
        if (order != null) {
            clearOrderCache(order);
        }
        return orderMapper.updateProfitStatus(orderNo, profitStatus);
    }

    @Override
    public int deleteById(Long id) {
        TransactionOrder order = findById(id);
        if (order != null) {
            clearOrderCache(order);
        }
        return orderMapper.deleteById(id);
    }

    private void clearOrderCache(TransactionOrder order) {
        if (order.getId() != null) {
            redisTemplate.delete(ORDER_CACHE_PREFIX + "id:" + order.getId());
        }
        if (order.getOrderNo() != null) {
            redisTemplate.delete(ORDER_CACHE_PREFIX + "no:" + order.getOrderNo());
        }
    }
}
