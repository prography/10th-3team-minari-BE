package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CreditUsageJpaRepository extends JpaRepository<CreditUsage, Long> {

    @Query("select usage from CreditUsage usage where usage.credit.id in :creditIds")
    List<CreditUsage> findAllByCreditIdIn(@Param("creditIds") Collection<Long> creditIds);
}
