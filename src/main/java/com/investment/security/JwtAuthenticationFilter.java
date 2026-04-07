package com.investment.security;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.entity.User;
import com.investment.enums.UserStatus;
import com.investment.repository.UserRepository;
import com.investment.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 *
 * @author Investment Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 检查Token是否在黑名单中
                if (tokenService.isBlacklisted(jwt)) {
                    log.warn("Token is blacklisted: {}", jwt.substring(0, 20) + "...");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                String userType = jwtTokenProvider.getUserTypeFromToken(jwt);

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

                // 检查用户状态（只禁止BANNED状态）
                if (user.getStatus() == UserStatus.BANNED) {
                    throw new BusinessException(ErrorCode.ACCOUNT_BANNED);
                }

                UserPrincipal principal = UserPrincipal.create(user);
                principal.setUserType(userType);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取JWT Token
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
