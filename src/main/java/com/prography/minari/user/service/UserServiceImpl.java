package com.prography.minari.user.service;

import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.UserRepository;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserFindResDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("id와 일치하는 user가 존재하지 않습니다."));
        return UserFindResDto.from(user);
    }
}
