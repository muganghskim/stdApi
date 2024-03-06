package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Cart;
import com.stdApi.pacificOcean.model.Category;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember(Member member);

    @Query(value = "SELECT s.pdNo FROM cart s WHERE s.userNo = :memberId", nativeQuery = true)
    List<Long> findProductNumber(Long memberId);

    Optional<Cart> findByMemberAndProduct(Member member, Product product);

}
