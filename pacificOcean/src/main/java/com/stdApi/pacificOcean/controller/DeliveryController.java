package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Delivery;
import com.stdApi.pacificOcean.service.DeliveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Delivery Controller", description = "배송 관련된 API")
public class DeliveryController {

    @Data
    public static class DeliveryReq {
        private String userEmail;
        private String userAddress1;
        private String userAddress2;
        private String userAddress3;
    }

    @Data
    public static class DeliveryUpt {
        private Long deliveryId;
        private String userAddress1;
        private String userAddress2;
        private String userAddress3;
    }

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService){
        this.deliveryService = deliveryService;
    }

    @PostMapping("/delivery/add")
    @ApiOperation(value = "배송지 추가", notes = "배송지를 추가합니다.")
    public ResponseEntity<Delivery> addDelivery(@RequestBody DeliveryReq request){
        try {
            return ResponseEntity.ok(deliveryService.addDelivery(request.getUserEmail(), request.getUserAddress1(), request.getUserAddress2(), request.getUserAddress3()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/delivery/{deliveryId}")
    @ApiOperation(value = "배송지 조회", notes = "배송지를 조회합니다.")
    public ResponseEntity<Delivery> getDelivery(@ApiParam(value = "배송지 조회 id", required = true) @PathVariable("deliveryId") Long deliveryId){
        try{
            return ResponseEntity.ok(deliveryService.getDelivery(deliveryId));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("delivery/update")
    @ApiOperation(value = "배송지 업데이트", notes = "배송지를 업데이트 합니다.")
    public ResponseEntity<Delivery> updateDelivery(@RequestBody DeliveryUpt request){
        try{
            return ResponseEntity.ok(deliveryService.updateDelivery(request.getDeliveryId(), request.getUserAddress1(), request.getUserAddress2(), request.getUserAddress3()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delivery/{deliveryId}")
    @ApiOperation(value = "배송지 삭제", notes = "배송지를 삭제합니다.")
    public ResponseEntity<Void> deleteDelivery(@ApiParam(value = "배송지 삭제 id", required = true) @PathVariable("deliveryId") Long deliveryId){
        try{
            deliveryService.deleteDelivery(deliveryId);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
