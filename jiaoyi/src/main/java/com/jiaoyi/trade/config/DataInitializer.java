package com.jiaoyi.trade.config;

import com.jiaoyi.trade.entity.Channel;
import com.jiaoyi.trade.entity.Merchant;
import com.jiaoyi.trade.mapper.ChannelMapper;
import com.jiaoyi.trade.mapper.MerchantMapper;
import com.jiaoyi.trade.util.RSAKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static volatile RSAKeyGenerator.KeyPair merchant1KeyPair;
    public static volatile RSAKeyGenerator.KeyPair merchant2KeyPair;

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始初始化测试数据...");

        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM merchant", Integer.class);
            
            if (count == null || count == 0) {
                initMerchants();
                initChannels();
                logger.info("测试数据初始化完成!");
            } else {
                logger.info("数据已存在，跳过初始化");
            }
        } catch (Exception e) {
            logger.warn("表可能不存在，尝试创建数据...");
            try {
                initMerchants();
                initChannels();
                logger.info("测试数据初始化完成!");
            } catch (Exception ex) {
                logger.error("数据初始化失败", ex);
            }
        }
    }

    private void initMerchants() throws Exception {
        merchant1KeyPair = RSAKeyGenerator.generateKeyPair();
        Merchant merchant1 = new Merchant();
        merchant1.setClientNo("M001");
        merchant1.setMerchantName("测试商户1");
        merchant1.setStatus(1);
        merchant1.setPublicKey(merchant1KeyPair.getPublicKey());
        merchant1.setPrivateKey(merchant1KeyPair.getPrivateKey());
        merchant1.setBalance(new BigDecimal("10000.00"));
        merchantMapper.insert(merchant1);
        logger.info("初始化商户M001完成");
        logger.info("M001公钥: {}", merchant1KeyPair.getPublicKey());
        logger.info("M001私钥: {}", merchant1KeyPair.getPrivateKey());

        merchant2KeyPair = RSAKeyGenerator.generateKeyPair();
        Merchant merchant2 = new Merchant();
        merchant2.setClientNo("M002");
        merchant2.setMerchantName("测试商户2");
        merchant2.setStatus(1);
        merchant2.setPublicKey(merchant2KeyPair.getPublicKey());
        merchant2.setPrivateKey(merchant2KeyPair.getPrivateKey());
        merchant2.setBalance(new BigDecimal("50000.00"));
        merchantMapper.insert(merchant2);
        logger.info("初始化商户M002完成");
    }

    private void initChannels() {
        String[][] channels = {
            {"C001", "支付宝通道", "1", "10000", "0", "1"},
            {"C002", "微信支付通道", "1", "10000", "0", "2"},
            {"C003", "银联支付通道", "1", "8000", "0", "3"},
            {"C004", "快捷支付通道", "1", "5000", "0", "4"},
            {"C005", "网银支付通道", "1", "3000", "0", "5"}
        };

        for (String[] ch : channels) {
            Channel channel = new Channel();
            channel.setChannelCode(ch[0]);
            channel.setChannelName(ch[1]);
            channel.setStatus(Integer.parseInt(ch[2]));
            channel.setMaxTransactions(Long.parseLong(ch[3]));
            channel.setCurrentTransactions(Long.parseLong(ch[4]));
            channel.setPriority(Integer.parseInt(ch[5]));
            channelMapper.insert(channel);
        }
        logger.info("初始化5条交易通道完成");
    }
}
