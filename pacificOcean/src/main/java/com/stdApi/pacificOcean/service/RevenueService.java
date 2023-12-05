package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Revenue;
import com.stdApi.pacificOcean.model.Transaction;
import com.stdApi.pacificOcean.repository.RevenueRepository;
import com.stdApi.pacificOcean.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class RevenueService {

    private final RevenueRepository revenueRepository;

    private final TransactionRepository transactionRepository;

    private final ProfitAndLossService profitAndLossService;

    @Autowired
    public RevenueService(RevenueRepository revenueRepository, TransactionRepository transactionRepository, ProfitAndLossService profitAndLossService) {
        this.revenueRepository = revenueRepository;
        this.transactionRepository = transactionRepository;
        this.profitAndLossService = profitAndLossService;
    }

    @Transactional
    public Revenue createRevenue(Long tid, int amount) {
        Transaction transaction = transactionRepository.findById(tid).orElseThrow(() -> new IllegalArgumentException("Invalid transaction id"));

        Revenue revenue = Revenue.builder()
                .transaction(transaction)
                .amount(amount)
                .build();

        revenueRepository.save(revenue);

        profitAndLossService.updateProfit(revenue.getRevenueId());

        return revenue;
    }

}
