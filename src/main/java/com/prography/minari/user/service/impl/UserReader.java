package com.prography.minari.user.service.impl;

import ch.qos.logback.core.spi.ErrorCodes;
import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static com.prography.minari.common.execption.ErrorCode.USER_NOT_FOUND;

@Slf4j
@ImplService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReader {
    private final UserRepository userRepository;

    public User read(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 사용자입니다. 회원가입을 먼저 진행해주세요. userId={}", userId);
                    return new ApiException(USER_NOT_FOUND);
                });
    }

    public User readByUUID(String uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
