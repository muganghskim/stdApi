package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Inventory;
import com.stdApi.pacificOcean.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InvenRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct(Product product);

    @Query(value = "SELECT * FROM Inventory WHERE pdNo = :pdNo", nativeQuery = true)
    Inventory findProductNumber(Long pdNo);

    Page<Inventory> findAll(Pageable pageable);
}
