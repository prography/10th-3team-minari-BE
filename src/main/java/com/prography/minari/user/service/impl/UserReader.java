package com.prography.minari.user.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> read(Long userId) {
        return userRepository.findById(userId);
    }
}
