package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.EventMeta;
import com.prography.minari.payment.service.impl.EventTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventMetaRepository extends JpaRepository<EventMeta,Long> {

    @Query(value = "SELECT m FROM EventMeta m " +
            "WHERE m.trigger = :trigger " +
            "AND m.startDateTime <= :now " +
            "AND m.endDateTime >= :now")
    List<EventMeta> findAllByTriggerAndNowBetweenStartDateTimeAndEndDateTime(
            @Param("trigger") EventTrigger trigger,
            @Param("now") LocalDateTime now);
}
