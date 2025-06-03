package com.prography.minari.user.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.prography.minari.social.dto.social.UserInfoDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.prography.minari.social.dto.enums.SocialType.KAKAO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    private static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    void 회원가입() {

        // given
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set("image", "https://picsum.photos/640/640")
                .sample();

        User actualUser = userRepository.save(User.create("", KAKAO, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image()));

        UserJoinReqDto userJoinReqDto = fixtureMonkey.giveMeBuilder(UserJoinReqDto.class)
                .set("userId", actualUser.getId())
                .sample();

        // when
        UserJoinResDto expectedDto = userService.join(userJoinReqDto);

        // then
        assertNotNull(expectedDto);

        assertEquals(true, expectedDto.isRegistered());
        assertEquals(userJoinReqDto.emailSendTime(), expectedDto.emailSendTime());
        assertEquals(userJoinReqDto.studyExperienceLevel(), expectedDto.studyExperienceLevel());
        assertEquals(userJoinReqDto.workExperienceLevel(), expectedDto.workExperienceLevel());
        assertEquals(userJoinReqDto.domain(), expectedDto.domain());

    }

}
