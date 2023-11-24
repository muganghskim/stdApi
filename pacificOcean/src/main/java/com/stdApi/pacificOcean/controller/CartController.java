package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Cart;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.service.CartService;
import com.stdApi.pacificOcean.service.UserService;
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
    public ResponseEntity<Cart> addToCart(@RequestBody RegiCartReq regiCartReq) {

        return ResponseEntity.ok(cartService.addToCart(regiCartReq.getUserEmail(), regiCartReq.getPdNo(), regiCartReq.getQuantity()));
    }

    // 장바구니 전체 조회
    @GetMapping("/cart/{userEmail}")
    public ResponseEntity<List<Cart>> getCartsByUserId(@PathVariable("userEmail") String userEmail) {
        return ResponseEntity.ok(cartService.getCartsByUserId(userEmail));
    }

    // 장바구니 수량 업뎃 (cartId 이용하면 될듯)
    @PutMapping("/cart/update")
    public ResponseEntity<Cart> updateCart(@RequestBody UpdateCartReq updateCartReq) {
        return ResponseEntity.ok(cartService.updateCart(updateCartReq.getCartId(), updateCartReq.getQuantity()));
    }

    // 장바구니 삭제
    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("cartId") Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
