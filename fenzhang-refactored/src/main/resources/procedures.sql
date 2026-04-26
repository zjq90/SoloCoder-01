-- ============================================
-- 分润计算存储过程（修复校对规则问题）
-- 执行方式：在MySQL客户端中手动执行此文件
-- 修复：Illegal mix of collations 错误
-- ============================================

-- 先删除已存在的存储过程
DROP PROCEDURE IF EXISTS sp_calculate_profit_for_order;
DROP PROCEDURE IF EXISTS sp_calculate_profit_batch;

DELIMITER //

-- 存储过程：计算单个订单的分润
-- 参数：
--   p_order_no: 订单号
--   p_batch_no: 批次号
--   p_platform_code: 平台代理商代码
-- 返回：
--   p_result_code: 结果码 (0=成功, 1=失败)
--   p_result_msg: 结果消息
CREATE PROCEDURE sp_calculate_profit_for_order(
    IN p_order_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    IN p_batch_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    IN p_platform_code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    OUT p_result_code INT,
    OUT p_result_msg VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
)
proc_label: BEGIN
    DECLARE v_agent_code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_agent_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_transaction_amount DECIMAL(18, 2);
    DECLARE v_profit_status INT;
    
    DECLARE v_current_agent_code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_current_agent_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_current_profit_rate DECIMAL(10, 4);
    DECLARE v_parent_agent_code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_parent_profit_rate DECIMAL(10, 4);
    DECLARE v_platform_profit_rate DECIMAL(10, 4);
    
    DECLARE v_rate_diff DECIMAL(10, 4);
    DECLARE v_profit_amount DECIMAL(18, 2);
    
    DECLARE v_profit_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_detail_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_log_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    
    DECLARE v_account_id BIGINT;
    DECLARE v_before_balance DECIMAL(18, 2);
    DECLARE v_after_balance DECIMAL(18, 2);
    DECLARE v_total_profit DECIMAL(18, 2);
    
    DECLARE v_chain_level INT;
    DECLARE v_max_level INT DEFAULT 10;
    DECLARE v_msg VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_result_code = 1;
        SET p_result_msg = 'SQL执行异常';
        
        SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
        
        INSERT INTO fenzhang_log (log_no, batch_no, order_no, operation_type, log_level, message, detail)
        VALUES (v_log_no, p_batch_no, p_order_no, 'CALCULATE_PROFIT', 'ERROR', 
                CONCAT('订单[', p_order_no, ']分润计算异常'), 'SQL Exception');
        
        UPDATE transaction_order SET profit_status = 2 WHERE order_no = p_order_no COLLATE utf8mb4_unicode_ci;
    END;
    
    SET p_result_code = 0;
    SET p_result_msg = 'SUCCESS';
    
    START TRANSACTION;
    
    SELECT agent_code, agent_name, transaction_amount, profit_status 
    INTO v_agent_code, v_agent_name, v_transaction_amount, v_profit_status
    FROM transaction_order 
    WHERE order_no = p_order_no COLLATE utf8mb4_unicode_ci
    FOR UPDATE;
    
    IF v_profit_status IS NULL THEN
        SET p_result_code = 1;
        SET p_result_msg = CONCAT('订单不存在: ', p_order_no);
        ROLLBACK;
        LEAVE proc_label;
    END IF;
    
    IF v_profit_status != 0 THEN
        SET p_result_code = 1;
        SET p_result_msg = CONCAT('订单[', p_order_no, ']已处理，状态: ', v_profit_status);
        ROLLBACK;
        LEAVE proc_label;
    END IF;
    
    SELECT profit_rate INTO v_platform_profit_rate 
    FROM agent 
    WHERE agent_code = p_platform_code COLLATE utf8mb4_unicode_ci 
      AND status = 1;
    
    IF v_platform_profit_rate IS NULL THEN
        SET p_result_code = 1;
        SET p_result_msg = '平台配置不存在或已禁用';
        ROLLBACK;
        
        SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
        
        INSERT INTO fenzhang_log (log_no, batch_no, order_no, operation_type, log_level, message)
        VALUES (v_log_no, p_batch_no, p_order_no, 'CALCULATE_PROFIT', 'ERROR', 
                CONCAT('订单[', p_order_no, ']平台配置不存在'));
        
        UPDATE transaction_order SET profit_status = 2 WHERE order_no = p_order_no COLLATE utf8mb4_unicode_ci;
        LEAVE proc_label;
    END IF;
    
    SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
    
    INSERT INTO fenzhang_log (log_no, batch_no, order_no, operation_type, log_level, message)
    VALUES (v_log_no, p_batch_no, p_order_no, 'CALCULATE_PROFIT', 'INFO', 
            CONCAT('开始处理订单: ', p_order_no, ', 代理商: ', v_agent_code));
    
    SET v_current_agent_code = v_agent_code;
    SET v_chain_level = 1;
    
    agent_loop: WHILE v_current_agent_code IS NOT NULL 
                  AND v_current_agent_code != p_platform_code COLLATE utf8mb4_unicode_ci
                  AND v_chain_level <= v_max_level DO
        
        SELECT agent_name, profit_rate, parent_agent_code 
        INTO v_current_agent_name, v_current_profit_rate, v_parent_agent_code
        FROM agent 
        WHERE agent_code = v_current_agent_code COLLATE utf8mb4_unicode_ci 
          AND status = 1;
        
        IF v_current_profit_rate IS NULL THEN
            SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
            
            INSERT INTO fenzhang_log (log_no, batch_no, order_no, agent_code, operation_type, log_level, message)
            VALUES (v_log_no, p_batch_no, p_order_no, v_current_agent_code, 'CALCULATE_PROFIT', 'WARN', 
                    CONCAT('代理商[', v_current_agent_code, ']不存在或已禁用，跳过'));
            SET v_current_agent_code = NULL;
            LEAVE agent_loop;
        END IF;
        
        IF v_parent_agent_code IS NULL OR v_parent_agent_code = '' THEN
            SET v_parent_agent_code = p_platform_code;
            SET v_parent_profit_rate = v_platform_profit_rate;
        ELSE
            SELECT profit_rate INTO v_parent_profit_rate 
            FROM agent 
            WHERE agent_code = v_parent_agent_code COLLATE utf8mb4_unicode_ci 
              AND status = 1;
            
            IF v_parent_profit_rate IS NULL THEN
                SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
                
                INSERT INTO fenzhang_log (log_no, batch_no, order_no, agent_code, operation_type, log_level, message)
                VALUES (v_log_no, p_batch_no, p_order_no, v_current_agent_code, 'CALCULATE_PROFIT', 'WARN', 
                        CONCAT('上级代理商[', v_parent_agent_code, ']不存在或已禁用，跳过'));
                SET v_current_agent_code = NULL;
                LEAVE agent_loop;
            END IF;
        END IF;
        
        SET v_rate_diff = v_parent_profit_rate - v_current_profit_rate;
        
        IF v_rate_diff <= 0 THEN
            SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
            
            INSERT INTO fenzhang_log (log_no, batch_no, order_no, agent_code, operation_type, log_level, message)
            VALUES (v_log_no, p_batch_no, p_order_no, v_current_agent_code, 'CALCULATE_PROFIT', 'WARN', 
                    CONCAT('分润点差非正，跳过代理商: ', v_current_agent_code, 
                           ', 上级费率: ', v_parent_profit_rate, ', 当前费率: ', v_current_profit_rate));
            
            SET v_current_agent_code = v_parent_agent_code;
            SET v_chain_level = v_chain_level + 1;
            ITERATE agent_loop;
        END IF;
        
        SET v_profit_amount = ROUND(v_rate_diff * v_transaction_amount, 2);
        
        SET v_profit_no = CONCAT('PROFIT', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
        
        INSERT INTO profit_detail (
            profit_no, order_no, agent_code, agent_name, parent_agent_code,
            transaction_amount, platform_profit_rate, agent_profit_rate, 
            profit_rate_diff, profit_amount, profit_time
        ) VALUES (
            v_profit_no, p_order_no, v_current_agent_code, v_current_agent_name, v_parent_agent_code,
            v_transaction_amount, v_platform_profit_rate, v_current_profit_rate,
            v_rate_diff, v_profit_amount, NOW()
        );
        
        SELECT id, balance, total_profit INTO v_account_id, v_before_balance, v_total_profit
        FROM agent_account 
        WHERE agent_code = v_current_agent_code COLLATE utf8mb4_unicode_ci
        FOR UPDATE;
        
        IF v_account_id IS NULL THEN
            INSERT INTO agent_account (
                agent_code, agent_name, balance, total_profit, 
                withdraw_amount, frozen_amount, status
            ) VALUES (
                v_current_agent_code, v_current_agent_name, v_profit_amount, v_profit_amount,
                0, 0, 1
            );
            SET v_before_balance = 0;
            SET v_after_balance = v_profit_amount;
        ELSE
            SET v_after_balance = v_before_balance + v_profit_amount;
            SET v_total_profit = v_total_profit + v_profit_amount;
            
            UPDATE agent_account 
            SET balance = v_after_balance, 
                total_profit = v_total_profit,
                update_time = NOW()
            WHERE id = v_account_id;
        END IF;
        
        SET v_detail_no = CONCAT('DETAIL', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
        
        INSERT INTO account_detail (
            detail_no, agent_code, agent_name, order_no, profit_no,
            transaction_type, amount, before_balance, after_balance, remark
        ) VALUES (
            v_detail_no, v_current_agent_code, v_current_agent_name, p_order_no, v_profit_no,
            'PROFIT_IN', v_profit_amount, v_before_balance, v_after_balance, '分润入账'
        );
        
        SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
        
        SET v_msg = CONCAT('profitNo=', v_profit_no, ', rateDiff=', v_rate_diff, ', profitAmount=', v_profit_amount);
        
        INSERT INTO fenzhang_log (log_no, batch_no, order_no, agent_code, operation_type, log_level, message, detail)
        VALUES (v_log_no, p_batch_no, p_order_no, v_current_agent_code, 'CALCULATE_PROFIT', 'INFO', 
                CONCAT('代理商[', v_current_agent_code, ']分润计算成功，金额: ', v_profit_amount),
                v_msg);
        
        SET v_current_agent_code = v_parent_agent_code;
        SET v_chain_level = v_chain_level + 1;
        
    END WHILE;
    
    UPDATE transaction_order SET profit_status = 1 WHERE order_no = p_order_no COLLATE utf8mb4_unicode_ci;
    
    SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
    
    INSERT INTO fenzhang_log (log_no, batch_no, order_no, operation_type, log_level, message)
    VALUES (v_log_no, p_batch_no, p_order_no, 'CALCULATE_PROFIT', 'INFO', 
            CONCAT('订单[', p_order_no, ']分润计算完成'));
    
    COMMIT;
    
END //

-- 存储过程：批量计算分润
-- 参数：
--   p_batch_no: 批次号
--   p_platform_code: 平台代理商代码
--   p_batch_size: 每批处理数量
-- 返回：
--   p_success_count: 成功数量
--   p_fail_count: 失败数量
CREATE PROCEDURE sp_calculate_profit_batch(
    IN p_batch_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    IN p_platform_code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    IN p_batch_size INT,
    OUT p_success_count INT,
    OUT p_fail_count INT
)
BEGIN
    DECLARE v_order_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_result_code INT;
    DECLARE v_result_msg VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_log_no VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    DECLARE v_msg VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    
    DECLARE order_cursor CURSOR FOR 
        SELECT order_no FROM transaction_order 
        WHERE profit_status = 0 
        ORDER BY create_time ASC 
        LIMIT p_batch_size;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    SET p_success_count = 0;
    SET p_fail_count = 0;
    
    SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
    
    INSERT INTO fenzhang_log (log_no, batch_no, operation_type, log_level, message)
    VALUES (v_log_no, p_batch_no, 'BATCH_START', 'INFO', 
            CONCAT('批次开始处理，批次号: ', p_batch_no));
    
    OPEN order_cursor;
    
    order_loop: LOOP
        FETCH order_cursor INTO v_order_no;
        
        IF done THEN
            LEAVE order_loop;
        END IF;
        
        CALL sp_calculate_profit_for_order(v_order_no, p_batch_no, p_platform_code, v_result_code, v_result_msg);
        
        IF v_result_code = 0 THEN
            SET p_success_count = p_success_count + 1;
        ELSE
            SET p_fail_count = p_fail_count + 1;
            
            SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
            
            INSERT INTO fenzhang_log (log_no, batch_no, order_no, operation_type, log_level, message, detail)
            VALUES (v_log_no, p_batch_no, v_order_no, 'ORDER_SKIP', 'WARN', 
                    CONCAT('订单处理异常，已跳过: ', v_result_msg), NULL);
        END IF;
    END LOOP;
    
    CLOSE order_cursor;
    
    SET v_log_no = CONCAT('LOG', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), FLOOR(RAND() * 1000));
    
    SET v_msg = CONCAT('successCount=', p_success_count, ', failCount=', p_fail_count);
    
    INSERT INTO fenzhang_log (log_no, batch_no, operation_type, log_level, message, detail)
    VALUES (v_log_no, p_batch_no, 'BATCH_END', 'INFO', 
            CONCAT('批次处理完成，成功: ', p_success_count, ', 失败: ', p_fail_count),
            v_msg);
END //

DELIMITER ;
