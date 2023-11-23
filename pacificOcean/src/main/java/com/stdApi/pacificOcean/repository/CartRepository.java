package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Cart;
import com.stdApi.pacificOcean.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember(Member member);

}
