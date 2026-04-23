package com.qianzhi.service;

import cn.hutool.core.util.RandomUtil;
import com.qianzhi.dto.LoginDTO;
import com.qianzhi.entity.Agent;
import com.qianzhi.entity.User;
import com.qianzhi.mapper.AgentMapper;
import com.qianzhi.mapper.UserMapper;
import com.qianzhi.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void sendLoginCode(String phone) {
        String code = RandomUtil.randomNumbers(6);
        String key = "login:code:" + phone;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        
        System.out.println("验证码: " + code);
    }

    public Map<String, Object> login(LoginDTO loginDTO) {
        String phone = loginDTO.getPhone();
        String code = loginDTO.getCode();
        String password = loginDTO.getPassword();

        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (user.getStatus() != 1) {
            throw new IllegalArgumentException("用户已被禁用");
        }

        if (code != null && !code.isEmpty()) {
            String key = "login:code:" + phone;
            Object storedCode = redisTemplate.opsForValue().get(key);
            if (storedCode == null || !storedCode.toString().equals(code)) {
                throw new IllegalArgumentException("验证码错误或已过期");
            }
            redisTemplate.delete(key);
        } else if (password != null && !password.isEmpty()) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new IllegalArgumentException("密码错误");
            }
        } else {
            throw new IllegalArgumentException("请提供验证码或密码");
        }

        Agent agent = agentMapper.selectByUserId(user.getId());
        if (agent == null) {
            throw new IllegalArgumentException("代理商信息不存在");
        }

        String token = jwtTokenUtil.generateToken(user.getId(), user.getPhone(), agent.getId(), agent.getAgentLevel());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("phone", user.getPhone());
        result.put("agentId", agent.getId());
        result.put("agentName", agent.getAgentName());
        result.put("agentLevel", agent.getAgentLevel());

        return result;
    }

    public User getCurrentUser(Long userId) {
        return userMapper.selectById(userId);
    }
}
