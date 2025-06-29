package com.prography.minari.user.entity;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Slf4j
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

    private boolean isRegistered;

    private boolean isSubscribed;

    @Enumerated(value = STRING)
    private EmailSendTime emailSendTime;

    @Enumerated(value = STRING)
    private ExperienceLevel studyExperienceLevel;

    @Enumerated(value = STRING)
    private ExperienceLevel workExperienceLevel;

    @Enumerated(value = STRING)
    private Domain domain;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<Answer> answers = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private Seed seed;

    public static User create(String email, SocialType socialType, Long socialId, String name, String image, String uuid) {
        User user = new User();
        user.email = email;
        user.socialType = socialType;
        user.socialId = socialId;
        user.name = name;
        user.image = image;
        user.isRegistered = false;
        user.uuid = uuid;
        user.isDeleted = false;
        return user;
    }

    public void join(String email, Boolean isSubscribed, EmailSendTime emailSendTime, ExperienceLevel studyExperienceLevel, ExperienceLevel workExperienceLevel, Domain domain) {
        this.email = email;
        this.isSubscribed = isSubscribed;
        this.emailSendTime = emailSendTime;
        this.studyExperienceLevel = studyExperienceLevel;
        this.workExperienceLevel = workExperienceLevel;
        this.domain = domain;
        this.isRegistered = true;
    }

    public Long getDaysSinceJoined() {
        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate created = getCreatedDateTime().toLocalDate();
        return ChronoUnit.DAYS.between(created, now);
    }

    public List<Domain>getPreferDomains() {
        return List.of(domain,Domain.CS);
    }

    // 계정 7일후 삭제 처리를 위해 deletedAt 시간 적재
    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now().plusDays(7);
    }

}
