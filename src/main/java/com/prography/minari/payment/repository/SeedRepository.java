package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.Seed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {
    @Query("select s from Seed s where s.user.id=:userId")
    Optional<Seed> findByUserId(@Param("userId") Long userId);
}
