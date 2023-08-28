package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserEmail(String userEmail);

}
