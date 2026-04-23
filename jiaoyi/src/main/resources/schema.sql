-- 商户表
CREATE TABLE IF NOT EXISTS merchant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_no VARCHAR(50) NOT NULL UNIQUE,
    merchant_name VARCHAR(100) NOT NULL,
    status SMALLINT NOT NULL DEFAULT 1,
    public_key TEXT NOT NULL,
    private_key TEXT NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 交易通道表
CREATE TABLE IF NOT EXISTS channel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel_code VARCHAR(50) NOT NULL UNIQUE,
    channel_name VARCHAR(100) NOT NULL,
    status SMALLINT NOT NULL DEFAULT 1,
    max_transactions BIGINT NOT NULL DEFAULT 10000,
    current_transactions BIGINT NOT NULL DEFAULT 0,
    priority INT NOT NULL DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 交易请求表（保存商户请求数据）
CREATE TABLE IF NOT EXISTS transaction_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(64) NOT NULL UNIQUE,
    client_no VARCHAR(50) NOT NULL,
    version VARCHAR(20) NOT NULL,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'CNY',
    notify_url VARCHAR(255),
    return_url VARCHAR(255),
    subject VARCHAR(255),
    body TEXT,
    attach TEXT,
    status SMALLINT DEFAULT 0,
    sign VARCHAR(512),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 通道请求表（保存请求交易通道的数据）
CREATE TABLE IF NOT EXISTS channel_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    channel_code VARCHAR(50) NOT NULL,
    channel_order_no VARCHAR(64) NOT NULL,
    request_data TEXT,
    response_data TEXT,
    response_code VARCHAR(50),
    response_msg VARCHAR(255),
    status SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 交易记录表（保存最终交易数据）
CREATE TABLE IF NOT EXISTS transaction_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(64) NOT NULL UNIQUE,
    client_no VARCHAR(50) NOT NULL,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    channel_code VARCHAR(50),
    channel_order_no VARCHAR(64),
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'CNY',
    status SMALLINT DEFAULT 0,
    notify_url VARCHAR(255),
    subject VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_tr_client_no ON transaction_request(client_no);
CREATE INDEX IF NOT EXISTS idx_tr_order_no ON transaction_request(order_no);
CREATE INDEX IF NOT EXISTS idx_tr_uuid ON transaction_request(uuid);

CREATE INDEX IF NOT EXISTS idx_cr_transaction_id ON channel_request(transaction_id);
CREATE INDEX IF NOT EXISTS idx_cr_channel_code ON channel_request(channel_code);

CREATE INDEX IF NOT EXISTS idx_tre_client_no ON transaction_record(client_no);
CREATE INDEX IF NOT EXISTS idx_tre_order_no ON transaction_record(order_no);
CREATE INDEX IF NOT EXISTS idx_tre_status ON transaction_record(status);
