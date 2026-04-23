-- 平台配置（存储平台分润点）
INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (0, 'PLATFORM', '平台', 0, NULL, 0, 0.3000, 1);

-- 一级代理
INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (1, 'A001', '一级代理-张三', 0, 'PLATFORM', 1, 0.2000, 1);

INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (2, 'A002', '一级代理-李四', 0, 'PLATFORM', 1, 0.2200, 1);

-- 二级代理
INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (3, 'B001', '二级代理-王五', 1, 'A001', 2, 0.1500, 1);

INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (4, 'B002', '二级代理-赵六', 1, 'A001', 2, 0.1400, 1);

INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (5, 'B003', '二级代理-钱七', 2, 'A002', 2, 0.1600, 1);

-- 三级代理
INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (6, 'C001', '三级代理-孙八', 3, 'B001', 3, 0.1000, 1);

INSERT INTO agent (id, agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES (7, 'C002', '三级代理-周九', 3, 'B001', 3, 0.0900, 1);

-- 代理账户初始化
INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('A001', '一级代理-张三', 0.00, 0.00, 0.00, 0.00, 1);

INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('A002', '一级代理-李四', 0.00, 0.00, 0.00, 0.00, 1);

INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('B001', '二级代理-王五', 0.00, 0.00, 0.00, 0.00, 1);

INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('B002', '二级代理-赵六', 0.00, 0.00, 0.00, 0.00, 1);

INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('B003', '二级代理-钱七', 0.00, 0.00, 0.00, 0.00, 1);

INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('C001', '三级代理-孙八', 0.00, 0.00, 0.00, 0.00, 1);

INSERT INTO agent_account (agent_code, agent_name, balance, total_profit, withdraw_amount, frozen_amount, status)
VALUES ('C002', '三级代理-周九', 0.00, 0.00, 0.00, 0.00, 1);

-- 交易订单数据（未计算分润）
INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORD20260420001', 'C001', '三级代理-孙八', 10000.00, 1, 0, '2026-04-20 10:30:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORD20260420002', 'C002', '三级代理-周九', 5000.00, 1, 0, '2026-04-20 11:15:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORD20260421001', 'B001', '二级代理-王五', 20000.00, 1, 0, '2026-04-21 09:00:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORD20260421002', 'C001', '三级代理-孙八', 8000.00, 1, 0, '2026-04-21 14:20:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORD20260422001', 'B003', '二级代理-钱七', 15000.00, 1, 0, '2026-04-22 16:45:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORD20260422002', 'A001', '一级代理-张三', 25000.00, 1, 0, '2026-04-22 17:30:00');
