package com.fenzhang.service.impl;

import com.fenzhang.entity.AgentAccount;
import com.fenzhang.mapper.AgentAccountMapper;
import com.fenzhang.service.AgentAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AgentAccountServiceImpl implements AgentAccountService {

    private static final Logger logger = LoggerFactory.getLogger(AgentAccountServiceImpl.class);

    private static final String ACCOUNT_CACHE_PREFIX = "account:";

    @Resource
    private AgentAccountMapper accountMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AgentAccount findById(Long id) {
        String cacheKey = ACCOUNT_CACHE_PREFIX + "id:" + id;
        AgentAccount account = (AgentAccount) redisTemplate.opsForValue().get(cacheKey);
        if (account != null) {
            return account;
        }
        account = accountMapper.findById(id);
        if (account != null) {
            redisTemplate.opsForValue().set(cacheKey, account, 5, TimeUnit.MINUTES);
        }
        return account;
    }

    @Override
    public AgentAccount findByAgentCode(String agentCode) {
        String cacheKey = ACCOUNT_CACHE_PREFIX + "code:" + agentCode;
        AgentAccount account = (AgentAccount) redisTemplate.opsForValue().get(cacheKey);
        if (account != null) {
            return account;
        }
        account = accountMapper.findByAgentCode(agentCode);
        if (account != null) {
            redisTemplate.opsForValue().set(cacheKey, account, 5, TimeUnit.MINUTES);
        }
        return account;
    }

    @Override
    public List<AgentAccount> findAll() {
        return accountMapper.findAll();
    }

    @Override
    public int save(AgentAccount account) {
        clearAccountCache(account);
        return accountMapper.insert(account);
    }

    @Override
    public int update(AgentAccount account) {
        clearAccountCache(account);
        return accountMapper.update(account);
    }

    @Override
    public int updateBalance(String agentCode, BigDecimal balance, BigDecimal totalProfit) {
        redisTemplate.delete(ACCOUNT_CACHE_PREFIX + "code:" + agentCode);
        return accountMapper.updateBalance(agentCode, balance, totalProfit);
    }

    @Override
    public int deleteById(Long id) {
        AgentAccount account = findById(id);
        if (account != null) {
            clearAccountCache(account);
        }
        return accountMapper.deleteById(id);
    }

    private void clearAccountCache(AgentAccount account) {
        if (account.getId() != null) {
            redisTemplate.delete(ACCOUNT_CACHE_PREFIX + "id:" + account.getId());
        }
        if (account.getAgentCode() != null) {
            redisTemplate.delete(ACCOUNT_CACHE_PREFIX + "code:" + account.getAgentCode());
        }
    }
}
