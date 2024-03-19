package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.exception.InvenLackedException;
import com.stdApi.pacificOcean.model.*;
import com.stdApi.pacificOcean.repository.InvenRepository;
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

    private final InvenRepository invenRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository, InvenRepository invenRepository) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.invenRepository = invenRepository;
    }

    @Transactional
    public OrderItemDTO createOrderItem(String userEmail, Long pdNo, int quantity, int price) {
        Member member = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid user email"));
        Product product = productRepository.findByPdNo(pdNo).orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        // 재고 존재 여부 및 수량 확인
        Inventory inventory = invenRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("재고를 찾을 수 없습니다."));
        if (inventory.getQuantity() < quantity) {
            throw new InvenLackedException("재고가 부족합니다.");
        }

        OrderItem orderItem = OrderItem.builder()
                .member(member)
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();

        orderItemRepository.save(orderItem);

        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemId(orderItem.getOrderItemId());

        return dto;
    }
}
