package com.qianzhi.service;

import com.qianzhi.dto.WithdrawDTO;
import com.qianzhi.entity.Account;
import com.qianzhi.entity.AccountDetail;
import com.qianzhi.entity.WithdrawRecord;
import com.qianzhi.mapper.AccountDetailMapper;
import com.qianzhi.mapper.AccountMapper;
import com.qianzhi.mapper.WithdrawRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private WithdrawRecordMapper withdrawRecordMapper;

    @Autowired
    private AccountDetailMapper accountDetailMapper;

    @Value("${withdraw.fee-rate:0.06}")
    private BigDecimal feeRate;

    public Account getByAgentId(Long agentId) {
        return accountMapper.selectByAgentId(agentId);
    }

    public Account getById(Long id) {
        return accountMapper.selectById(id);
    }

    public List<Account> list(Long agentId) {
        return accountMapper.selectList(agentId);
    }

    @Transactional
    public void createAccount(Long agentId) {
        Account account = new Account();
        account.setAgentId(agentId);
        account.setBalance(BigDecimal.ZERO);
        account.setFrozenAmount(BigDecimal.ZERO);
        account.setTotalIncome(BigDecimal.ZERO);
        account.setTotalWithdraw(BigDecimal.ZERO);
        account.setStatus(1);
        accountMapper.insert(account);
    }

    @Transactional
    public Map<String, Object> withdraw(Long agentId, WithdrawDTO dto) {
        Account account = accountMapper.selectByAgentId(agentId);
        if (account == null) {
            throw new IllegalArgumentException("账户不存在");
        }

        if (account.getStatus() != 1) {
            throw new IllegalArgumentException("账户已被冻结");
        }

        if (dto.getAmount().compareTo(account.getBalance()) > 0) {
            throw new IllegalArgumentException("余额不足");
        }

        BigDecimal withdrawAmount = dto.getAmount();
        BigDecimal feeAmount = withdrawAmount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actualAmount = withdrawAmount.subtract(feeAmount);

        WithdrawRecord record = new WithdrawRecord();
        record.setAccountId(account.getId());
        record.setAgentId(agentId);
        record.setAmount(withdrawAmount);
        record.setFeeRate(feeRate);
        record.setFeeAmount(feeAmount);
        record.setActualAmount(actualAmount);
        record.setBankName(dto.getBankName());
        record.setBankAccount(dto.getBankAccount());
        record.setBankHolder(dto.getBankHolder());
        record.setStatus(0);
        withdrawRecordMapper.insert(record);

        BigDecimal balanceBefore = account.getBalance();
        account.setBalance(balanceBefore.subtract(withdrawAmount));
        account.setTotalWithdraw(account.getTotalWithdraw().add(withdrawAmount));
        accountMapper.update(account);

        AccountDetail detail = new AccountDetail();
        detail.setAccountId(account.getId());
        detail.setAgentId(agentId);
        detail.setType(2);
        detail.setAmount(withdrawAmount.negate());
        detail.setBalanceBefore(balanceBefore);
        detail.setBalanceAfter(account.getBalance());
        detail.setRelatedId(record.getId());
        detail.setDescription("提现申请-" + dto.getBankName());
        accountDetailMapper.insert(detail);

        Map<String, Object> result = new HashMap<>();
        result.put("withdrawId", record.getId());
        result.put("amount", withdrawAmount);
        result.put("feeAmount", feeAmount);
        result.put("actualAmount", actualAmount);
        result.put("feeRate", feeRate);

        return result;
    }

    @Transactional
    public void addProfit(Long agentId, BigDecimal amount, Long relatedId, String description) {
        Account account = accountMapper.selectByAgentId(agentId);
        if (account == null) {
            throw new IllegalArgumentException("账户不存在");
        }

        BigDecimal balanceBefore = account.getBalance();
        account.setBalance(balanceBefore.add(amount));
        account.setTotalIncome(account.getTotalIncome().add(amount));
        accountMapper.update(account);

        AccountDetail detail = new AccountDetail();
        detail.setAccountId(account.getId());
        detail.setAgentId(agentId);
        detail.setType(1);
        detail.setAmount(amount);
        detail.setBalanceBefore(balanceBefore);
        detail.setBalanceAfter(account.getBalance());
        detail.setRelatedId(relatedId);
        detail.setDescription(description);
        accountDetailMapper.insert(detail);
    }

    public Map<String, Object> getAccountStats(Long agentId) {
        Account account = accountMapper.selectByAgentId(agentId);
        if (account == null) {
            throw new IllegalArgumentException("账户不存在");
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("balance", account.getBalance());
        stats.put("frozenAmount", account.getFrozenAmount());
        stats.put("totalIncome", account.getTotalIncome());
        stats.put("totalWithdraw", account.getTotalWithdraw());
        stats.put("feeRate", feeRate);

        return stats;
    }
}
