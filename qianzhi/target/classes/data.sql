-- 产品数据
INSERT INTO products (product_name, product_code, description, base_profit_rate, status) VALUES
('POS机标准费率', 'POS_001', '标准POS机刷卡费率产品', 0.0060, 1),
('POS机优惠费率', 'POS_002', '优惠POS机刷卡费率产品', 0.0055, 1),
('聚合支付', 'AGG_001', '微信支付宝聚合支付产品', 0.0038, 1),
('云闪付', 'CLOUD_001', '云闪付支付产品', 0.0030, 1);

-- 用户数据（默认密码：123456）
INSERT INTO users (phone, password, status, create_time) VALUES
('13800138001', '$2a$10$EqQ0hRqCvqN2bJqF3Z1U.eOYqJqF3Z1U.eOYqJqF3Z1U.eOYqJqF', 1, CURRENT_TIMESTAMP),
('13800138002', '$2a$10$EqQ0hRqCvqN2bJqF3Z1U.eOYqJqF3Z1U.eOYqJqF3Z1U.eOYqJqF', 1, CURRENT_TIMESTAMP),
('13800138003', '$2a$10$EqQ0hRqCvqN2bJqF3Z1U.eOYqJqF3Z1U.eOYqJqF3Z1U.eOYqJqF', 1, CURRENT_TIMESTAMP),
('13800138004', '$2a$10$EqQ0hRqCvqN2bJqF3Z1U.eOYqJqF3Z1U.eOYqJqF3Z1U.eOYqJqF', 1, CURRENT_TIMESTAMP);

-- 代理商数据 - 一级代理
INSERT INTO agents (user_id, agent_name, agent_code, agent_level, parent_id, contact, address, status) VALUES
(1, '北京总代', 'BJ001', 1, 0, '张总 13800138001', '北京市朝阳区XX大厦', 1),
(2, '上海总代', 'SH001', 1, 0, '李总 13800138002', '上海市浦东新区XX中心', 1);

-- 代理商数据 - 二级代理
INSERT INTO agents (user_id, agent_name, agent_code, agent_level, parent_id, contact, address, status) VALUES
(3, '北京朝阳代理', 'BJ001001', 2, 1, '王经理 13800138003', '北京市朝阳区XX路', 1),
(4, '北京海淀代理', 'BJ001002', 2, 1, '赵经理 13800138004', '北京市海淀区XX街', 1);

-- 分润配置 - 一级代理分润
INSERT INTO profit_configs (agent_id, product_id, profit_rate) VALUES
(1, 1, 0.0050),
(1, 2, 0.0045),
(1, 3, 0.0030),
(1, 4, 0.0025),
(2, 1, 0.0050),
(2, 2, 0.0045),
(2, 3, 0.0030),
(2, 4, 0.0025);

-- 分润配置 - 二级代理分润（低于上级）
INSERT INTO profit_configs (agent_id, product_id, profit_rate) VALUES
(3, 1, 0.0040),
(3, 2, 0.0035),
(3, 3, 0.0025),
(3, 4, 0.0020),
(4, 1, 0.0042),
(4, 2, 0.0038),
(4, 3, 0.0028),
(4, 4, 0.0022);

-- 账户数据
INSERT INTO accounts (agent_id, balance, frozen_amount, total_income, total_withdraw, status) VALUES
(1, 15680.50, 0.00, 25680.50, 10000.00, 1),
(2, 8920.30, 0.00, 18920.30, 10000.00, 1),
(3, 3560.80, 0.00, 8560.80, 5000.00, 1),
(4, 2180.60, 0.00, 7180.60, 5000.00, 1);

-- 商户数据
INSERT INTO merchants (agent_id, merchant_name, merchant_code, contact_name, contact_phone, address, status) VALUES
(3, '朝阳餐饮', 'CY001', '老板甲', '13900139001', '北京市朝阳区建国路88号', 1),
(3, '朝阳超市', 'CY002', '老板乙', '13900139002', '北京市朝阳区青年路25号', 1),
(4, '海淀饭店', 'HD001', '老板丙', '13900139003', '北京市海淀区中关村大街1号', 1),
(4, '海淀便利店', 'HD002', '老板丁', '13900139004', '北京市海淀区五道口路10号', 1);

-- 机器数据
INSERT INTO machines (agent_id, merchant_id, machine_sn, machine_name, machine_type, bind_time, status) VALUES
(3, 1, 'SN2024001001', 'POS机A1', '传统POS', '2024-01-15 10:30:00', 1),
(3, 1, 'SN2024001002', 'POS机A2', '传统POS', '2024-01-20 14:20:00', 1),
(3, 2, 'SN2024001003', 'POS机B1', '智能POS', '2024-02-10 09:15:00', 1),
(4, 3, 'SN2024002001', 'POS机C1', '传统POS', '2024-01-25 16:00:00', 1),
(4, 4, 'SN2024002002', 'POS机D1', '智能POS', '2024-03-01 11:45:00', 1);

-- 分润记录数据 - 一级代理北京总代的分润
INSERT INTO profit_records (agent_id, product_id, transaction_amount, profit_rate, profit_amount, merchant_id, machine_id, transaction_no, status, create_time) VALUES
(1, 1, 100000.00, 0.0050, 500.00, 1, 1, 'TXN20240401001', 1, '2024-04-01 10:00:00'),
(1, 1, 80000.00, 0.0050, 400.00, 2, 3, 'TXN20240401002', 1, '2024-04-01 11:00:00'),
(1, 3, 50000.00, 0.0030, 150.00, 1, 1, 'TXN20240402001', 1, '2024-04-02 09:30:00');

-- 分润记录数据 - 二级代理朝阳代理的分润
INSERT INTO profit_records (agent_id, product_id, transaction_amount, profit_rate, profit_amount, merchant_id, machine_id, transaction_no, status, create_time) VALUES
(3, 1, 100000.00, 0.0040, 400.00, 1, 1, 'TXN20240401001', 1, '2024-04-01 10:00:00'),
(3, 1, 80000.00, 0.0040, 320.00, 2, 3, 'TXN20240401002', 1, '2024-04-01 11:00:00'),
(3, 3, 50000.00, 0.0025, 125.00, 1, 1, 'TXN20240402001', 1, '2024-04-02 09:30:00');

-- 分润记录数据 - 二级代理海淀代理的分润
INSERT INTO profit_records (agent_id, product_id, transaction_amount, profit_rate, profit_amount, merchant_id, machine_id, transaction_no, status, create_time) VALUES
(4, 1, 60000.00, 0.0042, 252.00, 3, 4, 'TXN20240401003', 1, '2024-04-01 12:00:00'),
(4, 2, 40000.00, 0.0038, 152.00, 4, 5, 'TXN20240402002', 1, '2024-04-02 14:00:00');

-- 提现记录数据
INSERT INTO withdraw_records (account_id, agent_id, amount, fee_rate, fee_amount, actual_amount, bank_name, bank_account, bank_holder, status, remark, create_time) VALUES
(1, 1, 10000.00, 0.0600, 600.00, 9400.00, '中国工商银行', '6222021234567890', '张总', 2, '提现成功', '2024-03-15 10:00:00'),
(2, 2, 10000.00, 0.0600, 600.00, 9400.00, '中国建设银行', '6227001234567890', '李总', 2, '提现成功', '2024-03-18 14:00:00'),
(3, 3, 5000.00, 0.0600, 300.00, 4700.00, '中国农业银行', '6228481234567890', '王经理', 2, '提现成功', '2024-03-20 09:00:00'),
(4, 4, 5000.00, 0.0600, 300.00, 4700.00, '交通银行', '6222601234567890', '赵经理', 0, '待处理', '2024-04-20 16:00:00');

-- 账户明细数据 - 北京总代
INSERT INTO account_details (account_id, agent_id, type, amount, balance_before, balance_after, related_id, description, create_time) VALUES
(1, 1, 1, 500.00, 0.00, 500.00, 1, '分润收入-TXN20240401001', '2024-04-01 10:00:00'),
(1, 1, 1, 400.00, 500.00, 900.00, 2, '分润收入-TXN20240401002', '2024-04-01 11:00:00'),
(1, 1, 1, 150.00, 900.00, 1050.00, 3, '分润收入-TXN20240402001', '2024-04-02 09:30:00'),
(1, 1, 2, -10000.00, 15680.50, 5680.50, 1, '提现申请-工商银行', '2024-03-15 10:00:00');

-- 账户明细数据 - 朝阳代理
INSERT INTO account_details (account_id, agent_id, type, amount, balance_before, balance_after, related_id, description, create_time) VALUES
(3, 3, 1, 400.00, 0.00, 400.00, 4, '分润收入-TXN20240401001', '2024-04-01 10:00:00'),
(3, 3, 1, 320.00, 400.00, 720.00, 5, '分润收入-TXN20240401002', '2024-04-01 11:00:00'),
(3, 3, 1, 125.00, 720.00, 845.00, 6, '分润收入-TXN20240402001', '2024-04-02 09:30:00'),
(3, 3, 2, -5000.00, 8560.80, 3560.80, 3, '提现申请-农业银行', '2024-03-20 09:00:00');
