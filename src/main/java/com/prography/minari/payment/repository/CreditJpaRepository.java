package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditJpaRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByUserId(Long userId);
}
