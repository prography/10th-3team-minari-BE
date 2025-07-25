package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditJpaRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByUserId(Long userId);

    @Query("SELECT c FROM Credit c WHERE c.productId = :productId AND c.createdDateTime BETWEEN :start AND :end")
    Optional<Credit> findByProductIdAndCreatedAtToday(@Param("productId") Long productId,
                                                      @Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);
}
