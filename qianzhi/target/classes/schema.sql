-- 先删除已存在的表
DROP TABLE IF EXISTS account_details;
DROP TABLE IF EXISTS withdraw_records;
DROP TABLE IF EXISTS profit_records;
DROP TABLE IF EXISTS machines;
DROP TABLE IF EXISTS merchants;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS profit_configs;
DROP TABLE IF EXISTS agents;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- 用户表（手机号登录）
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(11) NOT NULL,
    password VARCHAR(255),
    login_code VARCHAR(6),
    code_expire_time TIMESTAMP,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (phone)
);

-- 产品表
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    base_profit_rate DECIMAL(5,4) DEFAULT 0.0000,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_code)
);

-- 代理商表
CREATE TABLE agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    agent_name VARCHAR(100) NOT NULL,
    agent_code VARCHAR(50) NOT NULL,
    agent_level INT NOT NULL,
    parent_id BIGINT DEFAULT 0,
    contact VARCHAR(50),
    address VARCHAR(200),
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (agent_code)
);

-- 分润配置表
CREATE TABLE profit_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    profit_rate DECIMAL(5,4) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_agent_product UNIQUE (agent_id, product_id)
);

-- 账户表
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    frozen_amount DECIMAL(15,2) DEFAULT 0.00,
    total_income DECIMAL(15,2) DEFAULT 0.00,
    total_withdraw DECIMAL(15,2) DEFAULT 0.00,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (agent_id)
);

-- 提现记录表
CREATE TABLE withdraw_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    agent_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    fee_rate DECIMAL(5,4) DEFAULT 0.0600,
    fee_amount DECIMAL(15,2) NOT NULL,
    actual_amount DECIMAL(15,2) NOT NULL,
    bank_name VARCHAR(100),
    bank_account VARCHAR(50),
    bank_holder VARCHAR(50),
    status INT DEFAULT 0,
    remark VARCHAR(500),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 账户明细表
CREATE TABLE account_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    agent_id BIGINT NOT NULL,
    type INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_before DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    related_id BIGINT,
    description VARCHAR(500),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分润记录表
CREATE TABLE profit_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    transaction_amount DECIMAL(15,2) NOT NULL,
    profit_rate DECIMAL(5,4) NOT NULL,
    profit_amount DECIMAL(15,2) NOT NULL,
    merchant_id BIGINT,
    machine_id BIGINT,
    transaction_no VARCHAR(50),
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 商户表
CREATE TABLE merchants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    merchant_name VARCHAR(100) NOT NULL,
    merchant_code VARCHAR(50) NOT NULL,
    contact_name VARCHAR(50),
    contact_phone VARCHAR(11),
    address VARCHAR(200),
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (merchant_code)
);

-- 机器表
CREATE TABLE machines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    merchant_id BIGINT,
    machine_sn VARCHAR(50) NOT NULL,
    machine_name VARCHAR(100),
    machine_type VARCHAR(50),
    bind_time TIMESTAMP,
    status INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (machine_sn)
);

-- 创建索引
CREATE INDEX idx_agents_parent ON agents(parent_id);
CREATE INDEX idx_agents_level ON agents(agent_level);
CREATE INDEX idx_agents_user ON agents(user_id);
CREATE INDEX idx_profit_configs_agent ON profit_configs(agent_id);
CREATE INDEX idx_accounts_agent ON accounts(agent_id);
CREATE INDEX idx_withdraw_records_agent ON withdraw_records(agent_id);
CREATE INDEX idx_withdraw_records_status ON withdraw_records(status);
CREATE INDEX idx_account_details_agent ON account_details(agent_id);
CREATE INDEX idx_account_details_time ON account_details(create_time);
CREATE INDEX idx_profit_records_agent ON profit_records(agent_id);
CREATE INDEX idx_profit_records_time ON profit_records(create_time);
CREATE INDEX idx_merchants_agent ON merchants(agent_id);
CREATE INDEX idx_machines_agent ON machines(agent_id);
CREATE INDEX idx_machines_merchant ON machines(merchant_id);
