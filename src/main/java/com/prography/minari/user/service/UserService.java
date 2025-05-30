package com.prography.minari.user.service;

import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;

    public UserFindResDto findById(Long id) {
        User user = userReader.read(id);
        return UserFindResDto.from(user);
    }

}
