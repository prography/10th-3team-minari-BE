package com.prography.minari.social.service;

import com.prography.minari.user.dto.UserLoginResDto;

public interface SocialService {

    UserLoginResDto login(String code);

}
