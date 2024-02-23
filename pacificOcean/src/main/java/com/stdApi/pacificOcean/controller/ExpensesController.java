package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Expenses;
import com.stdApi.pacificOcean.service.ExpensesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "Expenses Controller", description = "비용 관련된 API")
public class ExpensesController {
    @Data
    public static class ExpensesReq {
        private int salaries;

        private int bills;

        private int taxes;

        private int refund;
    }

    private final ExpensesService expensesService;

    @Autowired
    public ExpensesController(ExpensesService expensesService){
        this.expensesService = expensesService;
    }

    @PostMapping("/expenses/create")
    @ApiOperation(value = "비용 생성", notes = "발생한 비용을 기록합니다.")
    public ResponseEntity<Expenses> createExpenses(@RequestBody ExpensesReq request){
        try {
            return ResponseEntity.ok(expensesService.createExpenses(request.getSalaries(), request.getTaxes(), request.getBills(), request.getRefund()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
