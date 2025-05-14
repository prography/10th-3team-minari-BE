package com.prography.minari.user;

import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, Long socialId);
}
