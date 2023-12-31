package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Cart;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.service.CartService;
import com.stdApi.pacificOcean.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "Cart Controller", description = "장바구니 관련된 API")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService)
    {
        this.cartService = cartService;
    }

    @Data
    public static class RegiCartReq {
        String userEmail;
        Long pdNo;
        int quantity;
    }

    @Data
    public static class UpdateCartReq {
        Long cartId;

        int quantity;
    }

    // 장바구니 생성
    @PostMapping("/cart/create")
    @ApiOperation(value = "장바구니 생성", notes = "장바구니를 생성합니다.")
    public ResponseEntity<Cart> addToCart(@RequestBody RegiCartReq regiCartReq) {

        return ResponseEntity.ok(cartService.addToCart(regiCartReq.getUserEmail(), regiCartReq.getPdNo(), regiCartReq.getQuantity()));
    }

    // 장바구니 전체 조회
    @GetMapping("/cart/{userEmail}")
    @ApiOperation(value = "장바구니 전체 조회", notes = "유저의 장바구니를 조회합니다.")
    public ResponseEntity<List<Cart>> getCartsByUserId(@ApiParam(value = "장바구니 전체 조회 하려는 userEmail", required = true) @PathVariable("userEmail") String userEmail) {
        return ResponseEntity.ok(cartService.getCartsByUserId(userEmail));
    }

    // 장바구니 수량 업뎃 (cartId 이용하면 될듯)
    @PutMapping("/cart/update")
    @ApiOperation(value = "장바구니 수량 업데이트", notes = "유저의 장바구니를 상품을 업데이트 합니다.")
    public ResponseEntity<Cart> updateCart(@RequestBody UpdateCartReq updateCartReq) {
        return ResponseEntity.ok(cartService.updateCart(updateCartReq.getCartId(), updateCartReq.getQuantity()));
    }

    // 장바구니 삭제
    @DeleteMapping("/cart/{cartId}")
    @ApiOperation(value = "장바구니 삭제", notes = "유저의 장바구니를 삭제합니다.")
    public ResponseEntity<Void> deleteCart(@ApiParam(value = "장바구니 삭제 하려는 cartId", required = true) @PathVariable("cartId") Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
