package com.prography.minari.user.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.social.dto.enums.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "USERS")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "email")
    private String email;

    @Enumerated(value = STRING)
    private SocialType socialType;

    private Long socialId;

    private String name;

    private String image;

    private Boolean registered;

    public static User create(String email, SocialType socialType, Long socialId, String name, String image) {
        User user = new User();
        user.email = email;
        user.socialType = socialType;
        user.socialId = socialId;
        user.name = name;
        user.image = image;
        return user;
    }

}
