package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Expenses;
import com.stdApi.pacificOcean.model.ProfitandLoss;
import com.stdApi.pacificOcean.model.Revenue;
import com.stdApi.pacificOcean.repository.ExpensesRepository;
import com.stdApi.pacificOcean.repository.ProfitAndLossRepository;
import com.stdApi.pacificOcean.repository.RevenueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class ProfitAndLossService {
    @Autowired
    private ProfitAndLossRepository profitAndLossRepository;

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private ExpensesRepository expensesRepository;

    @Transactional
    public void updateProfit(Long revenueId) {
        Revenue revenue = revenueRepository.findById(revenueId).orElse(null);
//        Expenses expenses = expensesRepository.findById(expensesId).orElse(null);

        if(revenue != null) {
            ProfitandLoss profitAndLoss = profitAndLossRepository.findByRevenue(revenue);
            if(profitAndLoss == null) {
                profitAndLoss = new ProfitandLoss();
                profitAndLoss.setRevenue(revenue);
//                profitAndLoss.setExpenses(expenses);
            }
            int profit = revenue.getAmount();
            profitAndLoss.setProfit(profit);

            // totalProfit 필드 갱신
            profitAndLoss.setTotalProfit(profitAndLoss.getTotalProfit() + profit);

            profitAndLossRepository.save(profitAndLoss);
        }
    }

    @Transactional
    public void updateLoss(Long expensesId) {

        Expenses expenses = expensesRepository.findById(expensesId).orElse(null);

        if(expenses != null) {
            ProfitandLoss profitAndLoss = profitAndLossRepository.findByExpenses(expenses);
            if(profitAndLoss == null) {
                profitAndLoss = new ProfitandLoss();
                profitAndLoss.setExpenses(expenses);
            }
            int profit = -expenses.getTotalExpenses();
            profitAndLoss.setProfit(profit);

            // totalProfit 필드 갱신
            profitAndLoss.setTotalProfit(profitAndLoss.getTotalProfit() + profit);

            profitAndLossRepository.save(profitAndLoss);
        }
    }
}
