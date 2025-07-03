package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product as p where p.active is true")
    List<Product> findAllActive();
}
