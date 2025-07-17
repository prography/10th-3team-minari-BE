package com.prography.minari.payment.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.payment.service.impl.EventTrigger;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT_META")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMeta extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Enumerated(EnumType.STRING)
    private EventTrigger trigger;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    // 이벤트 설정값 (전략별로 해석) - JSON 형태 권장
    @Column(name = "metadata")
    private String metadata;

}
