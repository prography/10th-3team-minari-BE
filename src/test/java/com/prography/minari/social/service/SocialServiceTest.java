package com.prography.minari.social.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.dto.social.UserInfoDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import com.prography.minari.user.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.prography.minari.common.entity.Domain.BACKEND;
import static com.prography.minari.social.dto.enums.SocialType.KAKAO;
import static com.prography.minari.user.enums.EmailSendTime.AM_08;
import static com.prography.minari.user.enums.ExperienceLevel.NONE;
import static com.prography.minari.user.enums.ExperienceLevel.UNDER_1YEAR;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocialServiceTest {

    @Mock
    private UserRepository userRepository;

    private static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    void 최초_로그인시_User_객체_생성() {

        // given
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set("image", "https://picsum.photos/640/640")
                .sample();

        when(userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, userInfoDto.socialId()))
                .thenReturn(Optional.empty());

        when(userRepository.save(ArgumentMatchers.<User>any()))
                .thenReturn(User.create("", KAKAO, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image()));

        // when
        User savedUser = userRepository.findBySocialTypeAndSocialId(KAKAO, userInfoDto.socialId())
                .orElseGet(() -> userRepository.save(User.create("", KAKAO, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image())));

        // then
        assertEquals(KAKAO, savedUser.getSocialType());
        assertEquals(userInfoDto.socialId(), savedUser.getSocialId());
        assertEquals(userInfoDto.nickname(), savedUser.getName());
        assertEquals(userInfoDto.image(), savedUser.getImage());
        assertFalse(savedUser.isRegistered());
        verify(userRepository).save(ArgumentMatchers.<User>any());


    }

    @Test
    void 기존_회원_로그인시_User_객체_조회() {

        // Kakao API 통신 과정 생략

        // given
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set("image", "https://picsum.photos/640/640")
                .sample();

        User returnUser = User.create("", KAKAO, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image());
        returnUser.join(true, AM_08, UNDER_1YEAR, NONE, BACKEND);

        when(userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, userInfoDto.socialId()))
                .thenReturn(Optional.of(returnUser));

        // when
        User expectedUser = userRepository.findBySocialTypeAndSocialId(KAKAO, userInfoDto.socialId())
                .orElseGet(() -> userRepository.save(User.create("", KAKAO, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image())));

        // then
        assertEquals(expectedUser.getEmail(), returnUser.getEmail());
        assertEquals(expectedUser.getSocialId(), returnUser.getSocialId());
        assertEquals(expectedUser.getName(), returnUser.getName());
        assertEquals(expectedUser.getImage(), returnUser.getImage());
        assertEquals(expectedUser.getEmailSendTime(), returnUser.getEmailSendTime());
        assertEquals(expectedUser.getStudyExperienceLevel(), returnUser.getStudyExperienceLevel());
        assertEquals(expectedUser.getWorkExperienceLevel(), returnUser.getWorkExperienceLevel());
        assertEquals(expectedUser.getDomain(), returnUser.getDomain());
        assertEquals(expectedUser.isRegistered(), returnUser.isRegistered());
        assertEquals(expectedUser.isSubscribed(), returnUser.isSubscribed());
        verify(userRepository, never()).save(ArgumentMatchers.<User>any());
    }

}