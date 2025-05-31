package com.prography.minari.user.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import com.prography.minari.user.enums.PreferredPart;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ImplService
@RequiredArgsConstructor
@Transactional
public class UserWriter {

    private final UserRepository userRepository;

    public User join(User user, Boolean isSubscribed, EmailSendTime emailSendTime, ExperienceLevel studyExperienceLevel, ExperienceLevel workExperienceLevel, PreferredPart preferredPart) {
        user.join(isSubscribed, emailSendTime, studyExperienceLevel, workExperienceLevel, preferredPart);
        return userRepository.save(user);
    }

}
