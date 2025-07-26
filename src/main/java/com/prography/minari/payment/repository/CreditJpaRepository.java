package com.prography.minari.payment.repository;

import com.prography.minari.payment.dto.CreditProductDto;
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
    @Query("select new com.prography.minari.payment.dto.CreditProductDto(c, p) from Credit c left join Product p on c.productId=p.id where c.userId=:userId")
    List<CreditProductDto> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Credit c WHERE c.productId = :productId AND c.userId=:userId AND c.createdDateTime BETWEEN :start AND :end")
    Optional<Credit> findByProductIdAndCreatedAtToday(@Param("productId") Long productId,
                                                      @Param("userId") Long userId,
                                                      @Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);
}
