package com.prography.minari.user.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.payment.repository.SeedRepository;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import com.prography.minari.user.repository.UserRepository;
import com.prography.minari.mail.repository.MailAuthLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ImplService
@RequiredArgsConstructor
@Transactional
public class UserWriter {

    private final UserRepository userRepository;
    private final MailAuthLogRepository mailAuthLogRepository;
    private final SeedRepository seedRepository;

    public User join(User user, String email, Boolean isSubscribed, EmailSendTime emailSendTime, ExperienceLevel studyExperienceLevel, ExperienceLevel workExperienceLevel, Domain domain) {
        user.join(email, isSubscribed, emailSendTime, studyExperienceLevel, workExperienceLevel, domain);
        return userRepository.save(user);
    }

    public void delete(User user) {
        user.delete();
    }

    public void deleteAdmin(User user) {
        mailAuthLogRepository.deleteByUserId(user.getId());
        seedRepository.deleteByUserId(user.getId());
        userRepository.delete(user);
    }
}
