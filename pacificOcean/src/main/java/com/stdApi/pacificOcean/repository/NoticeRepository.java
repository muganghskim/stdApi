package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAll(Pageable pageable);
}
