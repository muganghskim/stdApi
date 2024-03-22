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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableScheduling
public class ProfitAndLossService {

    private final ProfitAndLossRepository profitAndLossRepository;


    private final RevenueRepository revenueRepository;


    private final ExpensesRepository expensesRepository;

    private final Map<String, Integer> cache = new ConcurrentHashMap<>();

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
//            profitAndLoss.setTotalProfit(profitAndLoss.getTotalProfit() + profit);

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
//            profitAndLoss.setTotalProfit(profitAndLoss.getTotalProfit() + profit);

            profitAndLossRepository.save(profitAndLoss);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProfitandLossDTO> findAll(Pageable pageable) {
        Page<ProfitandLoss> page = profitAndLossRepository.findAll(pageable);
        return page.map(entity -> {
            ProfitandLossDTO dto = new ProfitandLossDTO();
            dto.setPlId(entity.getPlId());
            dto.setProfit(entity.getProfit());
            if(entity.getExpenses() != null){
                dto.setSalaries(entity.getExpenses().getSalaries());
                dto.setBills(entity.getExpenses().getBills());
                dto.setRefund(entity.getExpenses().getRefund());
                dto.setTaxes(entity.getExpenses().getTaxes());
            }
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProfitandLossDTO> findByMonthAndYear(int month, int year, Pageable pageable) {
        Page<ProfitandLoss> page = profitAndLossRepository.findByMonthAndYear(month, year, pageable);
        return page.map(entity -> {
            ProfitandLossDTO dto = new ProfitandLossDTO();
            dto.setPlId(entity.getPlId());
            dto.setProfit(entity.getProfit());
            if(entity.getExpenses() != null){
                dto.setSalaries(entity.getExpenses().getSalaries());
                dto.setBills(entity.getExpenses().getBills());
                dto.setRefund(entity.getExpenses().getRefund());
                dto.setTaxes(entity.getExpenses().getTaxes());
            }
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProfitandLossDTO> findByDayMonthAndYear(int day, int month, int year, Pageable pageable) {
        Page<ProfitandLoss> page = profitAndLossRepository.findByDayMonthAndYear(day, month, year, pageable);
        return page.map(entity -> {
            ProfitandLossDTO dto = new ProfitandLossDTO();
            dto.setPlId(entity.getPlId());
            dto.setProfit(entity.getProfit());
            if(entity.getExpenses() != null){
                dto.setSalaries(entity.getExpenses().getSalaries());
                dto.setBills(entity.getExpenses().getBills());
                dto.setRefund(entity.getExpenses().getRefund());
                dto.setTaxes(entity.getExpenses().getTaxes());
            }
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }

    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    public void updateTotalProfitCache() {

        Integer totalProfit = profitAndLossRepository.calculateTotalProfit();

        cache.put("totalProfit", totalProfit != null ? totalProfit : 0);
    }

    public Integer getTotalProfit() {
        return cache.getOrDefault("totalProfit", 0);
    }

    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    public void updateMonthProfitCache() {
        // 현재 날짜에서 연도와 월을 추출
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();



        Integer monthProfit = profitAndLossRepository.calculateMonthAndYear(month, year);

        cache.put("monthProfit", monthProfit != null ? monthProfit : 0);

    }

    public Integer getMonthProfit() {
        return cache.getOrDefault("monthProfit", 0);
    }

    @Scheduled(cron = "0 */3 * * * *") // 매 3분마다 실행
    public void updateDayProfitCache() {
        // 현재 날짜에서 연도와 월을 추출
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();



        Integer dayProfit = profitAndLossRepository.calculateDayMonthAndYear(day, month, year);

        cache.put("dayProfit", dayProfit != null ? dayProfit : 0);
    }

    public Integer getDayProfit() {
        return cache.getOrDefault("dayProfit", 0);
    }

    public Map<String, Integer> getProfitData(){

        Map<String, Integer> profitData = new HashMap<>();

        profitData.put("totalProfit", getTotalProfit());
        profitData.put("monthProfit", getMonthProfit());
        profitData.put("dayProfit", getDayProfit());

        return  profitData;
    }

}
