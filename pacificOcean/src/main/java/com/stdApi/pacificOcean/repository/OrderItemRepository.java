package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.OrderItem;
import com.stdApi.pacificOcean.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Page<OrderItem> findAllBy(Pageable pageable);
    List<OrderItem> findByMember(Member member);
}
