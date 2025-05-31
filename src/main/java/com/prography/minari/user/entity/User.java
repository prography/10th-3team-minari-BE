package com.prography.minari.user.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import com.prography.minari.user.enums.PreferredPart;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "USERS")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Enumerated(value = STRING)
    private SocialType socialType;

    private Long socialId;

    private String name;

    private String image;

    private Boolean isRegistered;

    private Boolean isSubscribed;

    @Enumerated(value = STRING)
    private EmailSendTime emailSendTime;

    @Enumerated(value = STRING)
    private ExperienceLevel studyExperienceLevel;

    @Enumerated(value = STRING)
    private ExperienceLevel workExperienceLevel;

    @Enumerated(value = STRING)
    private PreferredPart preferredPart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id") // FK를 이쪽에서 관리
    private List<PreferDomain> preferDomains = new ArrayList<>();

    public static User create(String email, SocialType socialType, Long socialId, String name, String image) {
        User user = new User();
        user.email = email;
        user.socialType = socialType;
        user.socialId = socialId;
        user.name = name;
        user.image = image;
        user.isRegistered = false;
        return user;
    }

    public void join(Boolean isSubscribed, EmailSendTime emailSendTime, ExperienceLevel studyExperienceLevel, ExperienceLevel workExperienceLevel, PreferredPart preferredPart) {
        this.isSubscribed = isSubscribed;
        this.emailSendTime = emailSendTime;
        this.studyExperienceLevel = studyExperienceLevel;
        this.workExperienceLevel = workExperienceLevel;
        this.preferredPart = preferredPart;
        this.isRegistered = true;
    }

    public List<Domain> getPreferDomains() {
        return preferDomains.stream()
                .map(PreferDomain::getName)
                .toList();
    }

    public Long getDaysSinceJoined() {
        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate created = getCreatedDateTime().toLocalDate();
        return ChronoUnit.DAYS.between(created, now);
    }
}
