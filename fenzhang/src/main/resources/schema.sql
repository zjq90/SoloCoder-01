-- 代理商表
CREATE TABLE IF NOT EXISTS agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_code VARCHAR(50) NOT NULL UNIQUE,
    agent_name VARCHAR(100) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    parent_agent_code VARCHAR(50),
    agent_level INT DEFAULT 1,
    profit_rate DECIMAL(10, 4) DEFAULT 0.0000,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 交易订单表
CREATE TABLE IF NOT EXISTS transaction_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    agent_code VARCHAR(50) NOT NULL,
    agent_name VARCHAR(100),
    transaction_amount DECIMAL(18, 2) DEFAULT 0.00,
    order_status INT DEFAULT 0,
    profit_status INT DEFAULT 0,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分润明细表
CREATE TABLE IF NOT EXISTS profit_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    profit_no VARCHAR(50) NOT NULL UNIQUE,
    order_no VARCHAR(50) NOT NULL,
    agent_code VARCHAR(50) NOT NULL,
    agent_name VARCHAR(100),
    parent_agent_code VARCHAR(50),
    transaction_amount DECIMAL(18, 2) DEFAULT 0.00,
    platform_profit_rate DECIMAL(10, 4) DEFAULT 0.0000,
    agent_profit_rate DECIMAL(10, 4) DEFAULT 0.0000,
    profit_rate_diff DECIMAL(10, 4) DEFAULT 0.0000,
    profit_amount DECIMAL(18, 2) DEFAULT 0.00,
    profit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 代理账户表
CREATE TABLE IF NOT EXISTS agent_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_code VARCHAR(50) NOT NULL UNIQUE,
    agent_name VARCHAR(100),
    balance DECIMAL(18, 2) DEFAULT 0.00,
    total_profit DECIMAL(18, 2) DEFAULT 0.00,
    withdraw_amount DECIMAL(18, 2) DEFAULT 0.00,
    frozen_amount DECIMAL(18, 2) DEFAULT 0.00,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 账户明细表
CREATE TABLE IF NOT EXISTS account_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detail_no VARCHAR(50) NOT NULL UNIQUE,
    agent_code VARCHAR(50) NOT NULL,
    agent_name VARCHAR(100),
    order_no VARCHAR(50),
    profit_no VARCHAR(50),
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(18, 2) DEFAULT 0.00,
    before_balance DECIMAL(18, 2) DEFAULT 0.00,
    after_balance DECIMAL(18, 2) DEFAULT 0.00,
    remark VARCHAR(200),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分账日志表
CREATE TABLE IF NOT EXISTS fenzhang_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    log_no VARCHAR(50) NOT NULL UNIQUE,
    batch_no VARCHAR(50) NOT NULL,
    order_no VARCHAR(50),
    agent_code VARCHAR(50),
    operation_type VARCHAR(50) NOT NULL,
    log_level VARCHAR(20) DEFAULT 'INFO',
    message VARCHAR(500),
    detail TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 对账单表
CREATE TABLE IF NOT EXISTS statement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    statement_no VARCHAR(50) NOT NULL UNIQUE,
    statement_period VARCHAR(20) NOT NULL,
    agent_code VARCHAR(50) NOT NULL,
    agent_name VARCHAR(100),
    total_transaction_amount DECIMAL(18, 2) DEFAULT 0.00,
    total_profit_amount DECIMAL(18, 2) DEFAULT 0.00,
    transaction_count INT DEFAULT 0,
    profit_count INT DEFAULT 0,
    statement_status INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_agent_parent ON agent(parent_id);
CREATE INDEX IF NOT EXISTS idx_order_agent ON transaction_order(agent_code);
CREATE INDEX IF NOT EXISTS idx_order_profit_status ON transaction_order(profit_status);
CREATE INDEX IF NOT EXISTS idx_profit_order ON profit_detail(order_no);
CREATE INDEX IF NOT EXISTS idx_profit_agent ON profit_detail(agent_code);
CREATE INDEX IF NOT EXISTS idx_account_agent ON agent_account(agent_code);
CREATE INDEX IF NOT EXISTS idx_detail_agent ON account_detail(agent_code);
CREATE INDEX IF NOT EXISTS idx_log_batch ON fenzhang_log(batch_no);
CREATE INDEX IF NOT EXISTS idx_statement_agent ON statement(agent_code);
CREATE INDEX IF NOT EXISTS idx_statement_period ON statement(statement_period);
