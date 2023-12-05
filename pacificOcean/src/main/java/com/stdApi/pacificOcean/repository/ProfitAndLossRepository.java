package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Expenses;
import com.stdApi.pacificOcean.model.ProfitandLoss;
import com.stdApi.pacificOcean.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitAndLossRepository extends JpaRepository<ProfitandLoss, Long> {
    ProfitandLoss findByRevenue(Revenue revenue);

    ProfitandLoss findByExpenses(Expenses expenses);
}
