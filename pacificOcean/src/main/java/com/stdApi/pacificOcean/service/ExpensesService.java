package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Expenses;
import com.stdApi.pacificOcean.repository.ExpensesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;

    private final ProfitAndLossService profitAndLossService;

    @Autowired
    public ExpensesService(ExpensesRepository expensesRepository, ProfitAndLossService profitAndLossService){
        this.expensesRepository = expensesRepository;
        this.profitAndLossService = profitAndLossService;
    }

    @Transactional
    public Expenses createExpenses(int salaries, int bills, int taxes, int refund) {

        Expenses expenses = Expenses.builder()
                .salaries(salaries)
                .bills(bills)
                .taxes(taxes)
                .refund(refund)
                .totalExpenses(salaries + bills + taxes + refund)
                .build();

        expensesRepository.save(expenses);

        profitAndLossService.updateLoss(expenses.getExpenseId());

        return expenses;
    }

}
