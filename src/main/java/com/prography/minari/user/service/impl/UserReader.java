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

import static com.prography.minari.common.execption.ErrorCode.ACCOUNT_SOFT_DELETED;
import static com.prography.minari.common.execption.ErrorCode.USER_NOT_FOUND;

@Slf4j
@ImplService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReader {
    private final UserRepository userRepository;

    public User read(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.info("존재하지 않는 사용자입니다. 회원가입을 먼저 진행해주세요. userId={}", userId);
                return new ApiException(USER_NOT_FOUND);
            });

        // 삭제된 계정일 경우, 예외 처리
        if(user.isDeleted()) {
            log.info("해당 계정은 삭제된 계정입니다. userId={}", userId);
            throw new ApiException(ACCOUNT_SOFT_DELETED);
        }


        return user;
    }

    public User readByUUID(String uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
