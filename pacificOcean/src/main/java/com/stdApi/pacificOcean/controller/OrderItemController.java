package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.OrderItem;
import com.stdApi.pacificOcean.service.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "OrderItem Controller", description = "주문 관련된 API")
public class OrderItemController {
    @Data
    public static class OrderItemReq {

        private String userEmail;
        private Long pdNo;
        private int quantity;
        private int price;

    }

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/orderItem/create")
    @ApiOperation(value = "주문 생성", notes = "주문을 생성합니다.")
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItemReq request) {
        return ResponseEntity.ok(orderItemService.createOrderItem(request.getUserEmail(), request.getPdNo(), request.getQuantity(), request.getPrice()));
    }
}
