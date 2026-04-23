package com.jiaoyi.trade.service;

import com.jiaoyi.trade.common.ResponseCode;
import com.jiaoyi.trade.entity.Merchant;
import com.jiaoyi.trade.exception.BusinessException;
import com.jiaoyi.trade.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    public Merchant getMerchantByClientNo(String clientNo) {
        Merchant merchant = merchantMapper.selectByClientNo(clientNo);
        if (merchant == null) {
            throw new BusinessException(ResponseCode.MERCHANT_NOT_EXIST, "商户不存在");
        }
        return merchant;
    }

    public void validateMerchantStatus(Merchant merchant) {
        if (!merchant.isActive()) {
            throw new BusinessException(ResponseCode.MERCHANT_DISABLED, "商户已禁用");
        }
    }

    public Merchant getById(Long id) {
        return merchantMapper.selectById(id);
    }
}
