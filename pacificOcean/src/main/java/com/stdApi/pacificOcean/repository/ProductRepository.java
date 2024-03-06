package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPdName(String pdName);

    Optional<Product> findByPdNo(Long pdNo);

//    List<Product> findByPdNoIn(List<Long> pdNos); // 새로운 메서드 추가
}
