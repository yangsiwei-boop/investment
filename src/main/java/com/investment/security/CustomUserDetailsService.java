package com.investment.security;

import com.investment.entity.User;
import com.investment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户详情服务实现
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        User user = userRepository.findByPhone(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        return UserPrincipal.create(user);
    }

    /**
     * 根据用户ID加载用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    public UserDetails loadUserById(Long userId) {
        log.debug("Loading user by id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + userId));

        return UserPrincipal.create(user);
    }
}
