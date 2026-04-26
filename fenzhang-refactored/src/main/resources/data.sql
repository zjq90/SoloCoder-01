-- 插入平台代理商（最高级）
INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('PLATFORM', '平台总部', 0, NULL, 0, 0.1000, 1);

-- 插入一级代理商
INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('AGENT001', '一级代理商A', 1, 'PLATFORM', 1, 0.0800, 1);

INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('AGENT002', '一级代理商B', 1, 'PLATFORM', 1, 0.0750, 1);

-- 插入二级代理商
INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('AGENT003', '二级代理商A1', 2, 'AGENT001', 2, 0.0500, 1);

INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('AGENT004', '二级代理商A2', 2, 'AGENT001', 2, 0.0450, 1);

INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('AGENT005', '二级代理商B1', 3, 'AGENT002', 2, 0.0400, 1);

-- 插入三级代理商
INSERT INTO agent (agent_code, agent_name, parent_id, parent_agent_code, agent_level, profit_rate, status)
VALUES ('AGENT006', '三级代理商A1-1', 4, 'AGENT003', 3, 0.0200, 1);

-- 插入测试订单
INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORDER202401010001', 'AGENT006', '三级代理商A1-1', 10000.00, 1, 0, '2024-01-01 10:00:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORDER202401010002', 'AGENT003', '二级代理商A1', 5000.00, 1, 0, '2024-01-01 11:00:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORDER202401010003', 'AGENT005', '二级代理商B1', 20000.00, 1, 0, '2024-01-01 12:00:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORDER202401020001', 'AGENT001', '一级代理商A', 8000.00, 1, 0, '2024-01-02 09:00:00');

INSERT INTO transaction_order (order_no, agent_code, agent_name, transaction_amount, order_status, profit_status, transaction_time)
VALUES ('ORDER202401020002', 'AGENT006', '三级代理商A1-1', 15000.00, 1, 0, '2024-01-02 14:00:00');
