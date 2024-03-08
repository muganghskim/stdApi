package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Delivery;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByMember(Member member);
}
