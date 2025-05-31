package com.prography.minari.question.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.common.entity.Domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "QUESTIONS")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false,name = "content")
    private String content;
    @NotNull
    @Column(nullable = false,name = "answer")
    private String answer;
    @NotNull
    @Column(nullable = false,name = "tag")
    private String tag;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Domain domain;
    @NotNull
    @Column(nullable = false,name = "order_num")
    private int orderNum;
}
