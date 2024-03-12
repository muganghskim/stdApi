package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Expenses;
import com.stdApi.pacificOcean.model.ProfitandLoss;
import com.stdApi.pacificOcean.model.ProfitandLossDTO;
import com.stdApi.pacificOcean.model.Revenue;
import com.stdApi.pacificOcean.repository.ExpensesRepository;
import com.stdApi.pacificOcean.repository.ProfitAndLossRepository;
import com.stdApi.pacificOcean.repository.RevenueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProfitAndLossService {

    private final ProfitAndLossRepository profitAndLossRepository;


    private final RevenueRepository revenueRepository;


    private final ExpensesRepository expensesRepository;

    @Autowired
    public ProfitAndLossService(ProfitAndLossRepository profitAndLossRepository,RevenueRepository revenueRepository, ExpensesRepository expensesRepository ){
        this.profitAndLossRepository = profitAndLossRepository;
        this.revenueRepository = revenueRepository;
        this.expensesRepository = expensesRepository;
    }

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

    public Page<ProfitandLossDTO> findAll(Pageable pageable) {
        Page<ProfitandLoss> page = profitAndLossRepository.findAll(pageable);
        return page.map(entity -> {
            ProfitandLossDTO dto = new ProfitandLossDTO();
            dto.setPlId(entity.getPlId());
            dto.setProfit(entity.getProfit());
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }


    public Page<ProfitandLossDTO> findByMonthAndYear(int month, int year, Pageable pageable) {
        Page<ProfitandLoss> page = profitAndLossRepository.findByMonthAndYear(month, year, pageable);
        return page.map(entity -> {
            ProfitandLossDTO dto = new ProfitandLossDTO();
            dto.setPlId(entity.getPlId());
            dto.setProfit(entity.getProfit());
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }

    public Page<ProfitandLossDTO> findByDayMonthAndYear(int day, int month, int year, Pageable pageable) {
        Page<ProfitandLoss> page = profitAndLossRepository.findByDayMonthAndYear(day, month, year, pageable);
        return page.map(entity -> {
            ProfitandLossDTO dto = new ProfitandLossDTO();
            dto.setPlId(entity.getPlId());
            dto.setProfit(entity.getProfit());
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }
}
