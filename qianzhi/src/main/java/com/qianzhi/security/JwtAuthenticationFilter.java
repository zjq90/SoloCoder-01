package com.qianzhi.security;

import com.qianzhi.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = getTokenFromRequest(request);
        
        if (StringUtils.hasText(token) && jwtTokenUtil.validateToken(token)) {
            String phone = jwtTokenUtil.getPhoneFromToken(token);
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            Long agentId = jwtTokenUtil.getAgentIdFromToken(token);
            Integer agentLevel = jwtTokenUtil.getAgentLevelFromToken(token);

            UserPrincipal userPrincipal = new UserPrincipal();
            userPrincipal.setUserId(userId);
            userPrincipal.setPhone(phone);
            userPrincipal.setAgentId(agentId);
            userPrincipal.setAgentLevel(agentLevel);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_AGENT"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
