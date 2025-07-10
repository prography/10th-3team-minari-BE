package com.prography.minari.answer.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.question.entity.Question;
import com.prography.minari.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@Table(name = "ANSWERS")
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "reply", length = 1000)
    private String reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "memo")
    private String memo;

    @Column(name = "running_time")
    private Double runningTime;

    @Column(name = "answered_date")
    private LocalDate answeredDate;

}
