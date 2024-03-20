package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.ProfitandLossDTO;
import com.stdApi.pacificOcean.service.ProfitAndLossService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@Api(value = "ProfifandLoss Controller", description = "수익 관련된 API")
public class ProfitandLossController {

    private final ProfitAndLossService profitandLossService;

    @Autowired
    public ProfitandLossController(ProfitAndLossService profitandLossService) {
        this.profitandLossService = profitandLossService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "수익 전체 조회", notes = "수익을 전체조회합니다.")
    public Page<ProfitandLossDTO> getAllProfitAndLoss(Pageable pageable) {
        log.info("page: {}", pageable.getPageNumber());
        return profitandLossService.findAll(pageable);
    }

    @GetMapping("/month/{year}/{month}")
    @ApiOperation(value = "수익 월별조회", notes = "수익을 월별조회합니다.")
    public Page<ProfitandLossDTO> getProfitAndLossByMonthAndYear(@PathVariable int month, @PathVariable int year, Pageable pageable) {
        return profitandLossService.findByMonthAndYear(month, year, pageable);
    }

    @GetMapping("/day/{year}/{month}/{day}")
    @ApiOperation(value = "수익 일별조회", notes = "수익을 일별조회합니다.")
    public Page<ProfitandLossDTO> getProfitAndLossByDayMonthAndYear(@PathVariable int day, @PathVariable int month, @PathVariable int year, Pageable pageable) {
        return profitandLossService.findByDayMonthAndYear(day, month, year, pageable);
    }

    @GetMapping("/profit/data")
    @ApiOperation(value = "수익 통계", notes = "총수익, 월수익, 일수익 반환")
    public Map<String, Integer> getProfitData() {
        return profitandLossService.getProfitData();
    }
}
