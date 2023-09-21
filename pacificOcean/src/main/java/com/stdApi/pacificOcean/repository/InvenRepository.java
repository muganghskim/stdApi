package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Inventory;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvenRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findBypdNo(Product product);
}
