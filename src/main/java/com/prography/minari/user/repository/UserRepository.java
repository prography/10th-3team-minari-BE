package com.prography.minari.user.repository;

import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, Long socialId);

    Optional<User> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    boolean existsByEmail(String email);

}
