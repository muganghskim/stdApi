package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Transaction;
import com.stdApi.pacificOcean.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "Transaction Controller", description = "거래 관련된 API")
public class TransactionController {

    @Data
    public static class TransactionReq {

        private String userEmail;
        private Long deliveryId;
        private String rcvName;
        private String rcvPhn;
        private String tidStat;
        private String paymentMethod;
        private List<Long> orderItemIds;

        // getters and setters
    }
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/create")
    @ApiOperation(value = "거래 생성", notes = "거래를 생성합니다.")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionReq request) {
        try{
            transactionService.createTransaction(request.getUserEmail(), request.getDeliveryId(), request.getRcvName(), request.getRcvPhn(), request.getTidStat(), request.getPaymentMethod(), request.getOrderItemIds());
            return ResponseEntity.ok("200");
        } catch (Exception e) {
            log.error("taransaction 어디서 에러?", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
