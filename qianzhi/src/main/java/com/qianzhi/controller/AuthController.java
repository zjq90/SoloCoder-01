package com.qianzhi.controller;

import com.qianzhi.common.Result;
import com.qianzhi.dto.LoginDTO;
import com.qianzhi.entity.User;
import com.qianzhi.security.UserPrincipal;
import com.qianzhi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/send-code")
    public Result<Void> sendCode(@RequestParam String phone) {
        authService.sendLoginCode(phone);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = authService.login(loginDTO);
        return Result.success(result);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Result.error("用户未登录");
        }
        User user = authService.getCurrentUser(principal.getUserId());
        return Result.success(user);
    }
}
