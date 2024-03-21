package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Support;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {

    Page<Support> findAll(Pageable pageable);

    List<Support> findByMember(Member member);

}
