package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Cart;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.repository.CartRepository;
import com.stdApi.pacificOcean.repository.ProductRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartService {
    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart addToCart(String userEmail, Long pdNo, int quantity) {
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        Optional<Product> productOpt = productRepository.findByPdNo(pdNo);

        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        if (!productOpt.isPresent()){
            throw new RuntimeException("상품를 찾을 수 없습니다.");
        }

        Cart cart = Cart.builder()
                .member(memberOpt.get())
                .product(productOpt.get())
                .quantity(quantity)
                .build();

        return cartRepository.save(cart);
    }

    public List<Cart> getCartsByUserId(String userEmail) {
        Optional<Member> memberOptional = userRepository.findByUserEmail(userEmail);
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        Member member = memberOptional.get();
        return cartRepository.findByMember(member);
    }
}
