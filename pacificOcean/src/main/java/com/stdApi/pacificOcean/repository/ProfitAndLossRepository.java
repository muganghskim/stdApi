package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Expenses;
import com.stdApi.pacificOcean.model.ProfitandLoss;
import com.stdApi.pacificOcean.model.Revenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfitAndLossRepository extends JpaRepository<ProfitandLoss, Long> {

    //수입 업데이트
    ProfitandLoss findByRevenue(Revenue revenue);

    // 지출 업데이트
    ProfitandLoss findByExpenses(Expenses expenses);

    // 전체 조회
    Page<ProfitandLoss> findAll(Pageable pageable);

    // 월별 조회
    @Query("SELECT p FROM ProfitandLoss p WHERE FUNCTION('MONTH', p.createdAt) = :month AND FUNCTION('YEAR', p.createdAt) = :year")
    Page<ProfitandLoss> findByMonthAndYear(@Param("month") int month, @Param("year") int year, Pageable pageable);

    // 일별 조회
    @Query("SELECT p FROM ProfitandLoss p WHERE FUNCTION('DAY', p.createdAt) = :day AND FUNCTION('MONTH', p.createdAt) = :month AND FUNCTION('YEAR', p.createdAt) = :year")
    Page<ProfitandLoss> findByDayMonthAndYear(@Param("day") int day, @Param("month") int month, @Param("year") int year, Pageable pageable);
}
