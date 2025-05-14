package com.prography.minari.user.service;

import com.prography.minari.user.dto.UserFindResDto;

public interface UserService {
    UserFindResDto findById(Long id);
}
