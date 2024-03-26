package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.exception.EmailNotVerifiedException;
import com.stdApi.pacificOcean.exception.InvenLackedException;
import com.stdApi.pacificOcean.model.OrderItem;
import com.stdApi.pacificOcean.model.OrderItemDTO;
import com.stdApi.pacificOcean.model.SupportDTO;
import com.stdApi.pacificOcean.service.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Data
    public static class AdminOrderReq {
        private Long orderItemId;
        private String stat;
    }

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }


    @PostMapping("/orderItem/create")
    @ApiOperation(value = "주문 생성", notes = "주문을 생성합니다.")
    public ResponseEntity<List<OrderItemDTO>> createOrderItems(@RequestBody List<OrderItemReq> requests) {
        List<OrderItemDTO> createdOrderItems = new ArrayList<>();
        try {
            for (OrderItemReq request : requests) {
                OrderItemDTO createdOrderItem = orderItemService.createOrderItem(request.getUserEmail(), request.getPdNo(), request.getQuantity(), request.getPrice());
                createdOrderItems.add(createdOrderItem);
            }
            return ResponseEntity.ok(createdOrderItems);
        } catch (InvenLackedException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null); // 422
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/admin/orderItemAll")
    @ApiOperation(value = "관리자 주문 전체 조회", notes = "주문을 관리자가 전체조회합니다.")
    public Page<OrderItemDTO> getAdminOrderItems(Pageable pageable) {
        return orderItemService.getAdminOrderItems(pageable);
    }

    // todo : 유저 주문 전체 조회 문제 있음
    @GetMapping("/orderItem/all/{userEmail}")
    @ApiOperation(value = "유저 주문 전체 조회", notes = "유저가 주문을 전체조회합니다.")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable("userEmail") String userEmail){
        try{
            return ResponseEntity.ok(orderItemService.getOrderItems(userEmail));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/admin/orderItemUpdate")
    @ApiOperation(value = "관리자 주문 업데이트", notes = "관리자가 주문을 업데이트합니다.")
    public ResponseEntity<?> updateSupport(@RequestBody AdminOrderReq req){
        try {
            orderItemService.updateOrderItem(req.getOrderItemId(), req.getStat());
            return ResponseEntity.ok("200");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
