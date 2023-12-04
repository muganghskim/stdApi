package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.OrderItem;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.repository.OrderItemRepository;
import com.stdApi.pacificOcean.repository.ProductRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Slf4j
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderItem createOrderItem(String userEmail, Long pdNo, int quantity, int price) {
        Member member = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid user email"));
        Product product = productRepository.findByPdNo(pdNo).orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        OrderItem orderItem = OrderItem.builder()
                .member(member)
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();

        return orderItemRepository.save(orderItem);
    }
}
