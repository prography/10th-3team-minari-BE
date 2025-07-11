package com.prography.minari.question.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.common.entity.Domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "QUESTIONS")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, name = "content")
    private String content;
    @NotNull
    @Column(nullable = false, name = "answer")
    private String answer;
    @NotNull
    @Column(nullable = false, name = "tag")
    private String tag;
    @Column(nullable = false, name = "tag_detail")
    private String tagDetail;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Domain domain;
    @NotNull
    @Column(nullable = false, name = "order_num")
    private int orderNum;

    public List<String> getTags() {
        return Arrays.stream(tag.split(","))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String>getShuffledTags(int size) {
        List<String> tags = getTags();
        Collections.shuffle(tags);
        int count = Math.min(tags.size(), size);
        return tags.subList(0, count);
    }

}
