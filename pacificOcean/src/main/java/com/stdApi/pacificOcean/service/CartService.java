package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.*;
import com.stdApi.pacificOcean.repository.CartRepository;
import com.stdApi.pacificOcean.repository.ProductRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void addToCart(String userEmail, Long pdNo, int quantity) {
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        Optional<Product> productOpt = productRepository.findByPdNo(pdNo);

        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        if (!productOpt.isPresent()){
            throw new RuntimeException("상품를 찾을 수 없습니다.");
        }

        Member member = memberOpt.get();
        Product product = productOpt.get();

        // 해당 유저의 카트에서 해당 상품이 있는지 확인
        Optional<Cart> cartItemOpt = cartRepository.findByMemberAndProduct(member, product);

        if (cartItemOpt.isPresent()) {
            // 이미 카트에 해당 상품이 있는 경우 수량을 업데이트
            Cart cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepository.save(cartItem);
        } else {
            // 카트에 해당 상품이 없는 경우 새로운 카트 아이템 생성
            Cart cart = Cart.builder()
                    .member(member)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartRepository.save(cart);
        }
    }


    public List<CartDTO> getCartsByUserId(String userEmail) {
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        Member member = memberOpt.get();

        List<Cart> carts = cartRepository.findByMember(member);
        log.info("carts: {}", carts);
        List<Long> productIds = cartRepository.findProductNumber(member.getUserNo());
        log.info("productIds: {}", productIds);

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Long productId : productIds) {
            Optional<Product> productOpt = productRepository.findByPdNo(productId);
            productOpt.ifPresent(product -> {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setPdNo(product.getPdNo());
                productDTO.setPdName(product.getPdName());
                productDTO.setPdImg(product.getPdImg());
                productDTO.setPdPrice(product.getPdPrice());
                productDTOs.add(productDTO);
            });
        }

        List<CartDTO> cartDTOs = new ArrayList<>();
        for (Cart cart : carts) {
            CartDTO cartDTO = new CartDTO();
            for (ProductDTO productDTO : productDTOs) {
                if (cart.getProduct().getPdNo() == productDTO.getPdNo()) {
                    cartDTO.setCartId(cart.getCartId());
                    cartDTO.setPdNo(productDTO.getPdNo());
                    cartDTO.setPdName(productDTO.getPdName());
                    cartDTO.setPdImg(productDTO.getPdImg());
                    cartDTO.setPdPrice(productDTO.getPdPrice());
                    cartDTO.setPdQuantity(cart.getQuantity());
                    // 나머지 필드들도 필요에 따라 추가

                    // CartDTO를 리스트에 추가
                    cartDTOs.add(cartDTO);
                    break;
                }
            }
        }

        return cartDTOs;
    }

    @Transactional
    public Cart updateCart(Long cartId, int quantity){
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if(!cartOpt.isPresent()) {
            throw new RuntimeException("장바구니에 상품이 존재하지 않습니다.");
        }

        Cart cart = cartOpt.get();
        cart.setQuantity(quantity);

        return cart;
    }

    @Transactional
    public void removeCart(Long cartId, int quantityToRemove) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (!cartOpt.isPresent()) {
            throw new RuntimeException("장바구니에 상품이 존재하지 않습니다.");
        }

        Cart cart = cartOpt.get();
        int currentQuantity = cart.getQuantity();
        int updatedQuantity = currentQuantity - quantityToRemove;

        // 최소 수량을 0으로 설정하여 음수가 되지 않도록 합니다.
        updatedQuantity = Math.max(updatedQuantity, 0);

        cart.setQuantity(updatedQuantity);

    }

    @Transactional
    public void deleteCart(Long cartId){
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if(!cartOpt.isPresent()) {
            throw new RuntimeException("장바구니에 상품이 존재하지 않습니다.");
        }
        Cart cart = cartOpt.get();
        cartRepository.delete(cart);
    }
}
