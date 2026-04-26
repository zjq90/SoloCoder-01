package com.fenzhang.service.impl;

import com.fenzhang.entity.AccountDetail;
import com.fenzhang.entity.Agent;
import com.fenzhang.entity.AgentAccount;
import com.fenzhang.entity.FenzhangLog;
import com.fenzhang.mapper.AccountDetailMapper;
import com.fenzhang.mapper.AgentAccountMapper;
import com.fenzhang.mapper.FenzhangLogMapper;
import com.fenzhang.service.AgentAccountService;
import com.fenzhang.service.AgentService;
import com.fenzhang.util.NoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AgentAccountServiceImpl implements AgentAccountService {

    private static final Logger logger = LoggerFactory.getLogger(AgentAccountServiceImpl.class);

    private static final String REDIS_ACCOUNT_KEY_PREFIX = "fenzhang:account:";
    private static final long REDIS_EXPIRE_SECONDS = 1800;

    @Resource
    private AgentAccountMapper agentAccountMapper;

    @Resource
    private AccountDetailMapper accountDetailMapper;

    @Resource
    private FenzhangLogMapper fenzhangLogMapper;

    @Resource
    private AgentService agentService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AgentAccount findById(Long id) {
        logger.debug("根据ID查询代理账户: {}", id);
        return agentAccountMapper.findById(id);
    }

    @Override
    public AgentAccount findByAgentCode(String agentCode) {
        logger.debug("根据代理商代码查询账户: {}", agentCode);
        
        String cacheKey = REDIS_ACCOUNT_KEY_PREFIX + agentCode;
        AgentAccount cachedAccount = (AgentAccount) redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedAccount != null) {
            logger.debug("从缓存获取账户: {}", agentCode);
            return cachedAccount;
        }
        
        AgentAccount account = agentAccountMapper.findByAgentCode(agentCode);
        if (account != null) {
            redisTemplate.opsForValue().set(cacheKey, account, REDIS_EXPIRE_SECONDS, TimeUnit.SECONDS);
        }
        
        return account;
    }

    @Override
    public List<AgentAccount> findAll() {
        logger.debug("查询所有代理账户");
        return agentAccountMapper.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(AgentAccount account) {
        logger.info("保存代理账户: {}", account.getAgentCode());
        
        int result = agentAccountMapper.insert(account);
        
        if (result > 0) {
            clearAccountCache(account.getAgentCode());
            logger.info("代理账户保存成功: {}", account.getAgentCode());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AgentAccount account) {
        logger.info("更新代理账户: {}", account.getAgentCode());
        
        int result = agentAccountMapper.update(account);
        
        if (result > 0) {
            clearAccountCache(account.getAgentCode());
            logger.info("代理账户更新成功: {}", account.getAgentCode());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBalance(String agentCode, BigDecimal balance, BigDecimal totalProfit) {
        logger.info("更新账户余额，代理商: {}, 余额: {}, 总分润: {}", agentCode, balance, totalProfit);
        
        int result = agentAccountMapper.updateBalance(agentCode, balance, totalProfit);
        
        if (result > 0) {
            clearAccountCache(agentCode);
            logger.info("账户余额更新成功，代理商: {}", agentCode);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProfit(String agentCode, BigDecimal amount, String orderNo, String profitNo, String batchNo) {
        logger.info("添加分润，代理商: {}, 金额: {}, 订单: {}", agentCode, amount, orderNo);
        
        try {
            Agent agent = agentService.findByAgentCode(agentCode);
            if (agent == null) {
                logger.error("代理商不存在: {}", agentCode);
                return false;
            }

            AgentAccount account = findByAgentCode(agentCode);
            BigDecimal beforeBalance;
            BigDecimal afterBalance;
            BigDecimal newTotalProfit;

            if (account == null) {
                account = new AgentAccount();
                account.setAgentCode(agentCode);
                account.setAgentName(agent.getAgentName());
                account.setBalance(amount);
                account.setTotalProfit(amount);
                account.setWithdrawAmount(BigDecimal.ZERO);
                account.setFrozenAmount(BigDecimal.ZERO);
                account.setStatus(AgentAccount.STATUS_NORMAL);
                agentAccountMapper.insert(account);
                
                beforeBalance = BigDecimal.ZERO;
                afterBalance = amount;
            } else {
                beforeBalance = account.getBalance();
                afterBalance = beforeBalance.add(amount);
                newTotalProfit = account.getTotalProfit().add(amount);
                
                agentAccountMapper.updateBalance(agentCode, afterBalance, newTotalProfit);
            }

            AccountDetail detail = new AccountDetail();
            detail.setDetailNo(NoGenerator.generateDetailNo());
            detail.setAgentCode(agentCode);
            detail.setAgentName(agent.getAgentName());
            detail.setOrderNo(orderNo);
            detail.setProfitNo(profitNo);
            detail.setTransactionType(AccountDetail.TYPE_PROFIT_IN);
            detail.setAmount(amount);
            detail.setBeforeBalance(beforeBalance);
            detail.setAfterBalance(afterBalance);
            detail.setRemark("分润入账");
            accountDetailMapper.insert(detail);

            clearAccountCache(agentCode);

            saveLog(batchNo, orderNo, agentCode, FenzhangLog.OP_ACCOUNT_UPDATE, 
                    FenzhangLog.LEVEL_INFO, "账户更新成功，金额: " + amount, 
                    "profitNo=" + profitNo + ", amount=" + amount);

            logger.info("分润添加成功，代理商: {}, 金额: {}", agentCode, amount);
            return true;

        } catch (Exception e) {
            logger.error("添加分润失败，代理商: {}, 金额: {}", agentCode, amount, e);
            
            saveLog(batchNo, orderNo, agentCode, FenzhangLog.OP_ACCOUNT_UPDATE, 
                    FenzhangLog.LEVEL_ERROR, "账户更新失败: " + e.getMessage(), 
                    getStackTraceAsString(e));
            
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        logger.info("删除代理账户: {}", id);
        
        AgentAccount account = agentAccountMapper.findById(id);
        if (account != null) {
            clearAccountCache(account.getAgentCode());
        }
        
        return agentAccountMapper.deleteById(id);
    }

    private void clearAccountCache(String agentCode) {
        String cacheKey = REDIS_ACCOUNT_KEY_PREFIX + agentCode;
        redisTemplate.delete(cacheKey);
        logger.debug("清除账户缓存: {}", agentCode);
    }

    private void saveLog(String batchNo, String orderNo, String agentCode, 
                         String operationType, String logLevel, String message, String detail) {
        try {
            FenzhangLog log = new FenzhangLog();
            log.setLogNo(NoGenerator.generateLogNo());
            log.setBatchNo(batchNo);
            log.setOrderNo(orderNo);
            log.setAgentCode(agentCode);
            log.setOperationType(operationType);
            log.setLogLevel(logLevel);
            log.setMessage(message);
            log.setDetail(detail);
            fenzhangLogMapper.insert(log);
        } catch (Exception e) {
            logger.error("保存日志失败: operationType={}, message={}", operationType, message, e);
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
