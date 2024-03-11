package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Inventory;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InvenRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct(Product product);

    @Query(value = "SELECT s FROM cart s WHERE s.pdNo = :pdNo", nativeQuery = true)
    Inventory findProductNumber(Long pdNo);
}
