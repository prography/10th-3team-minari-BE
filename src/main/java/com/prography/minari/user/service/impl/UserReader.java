package com.prography.minari.user.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ImplService
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User read(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
    }
}
