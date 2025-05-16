package com.prography.minari.question.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
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

    @Column(nullable = false,name = "content")
    private String content;
    @Column(nullable = false,name = "answer")
    private String answer;
    @Column(nullable = false,name = "tag")
    private String tag;
}
