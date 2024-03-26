package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.exception.InvenLackedException;
import com.stdApi.pacificOcean.model.*;
import com.stdApi.pacificOcean.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InvenRepository invenRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository, InvenRepository invenRepository, TransactionRepository transactionRepository) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.invenRepository = invenRepository;
        this.transactionRepository = transactionRepository;
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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<OrderItemDTO> getAdminOrderItems(Pageable pageable){
        Page<OrderItem> orderItems = orderItemRepository.findAll(pageable);

        return orderItems.map(entity -> {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setOrderItemId(entity.getOrderItemId());
            dto.setQuantity(entity.getQuantity());
            dto.setPrice(entity.getPrice());
            dto.setUserEmail(entity.getMember().getUserEmail());
            dto.setAddress1(entity.getTransaction().getAddress1());
            dto.setAddress2(entity.getTransaction().getAddress2());
            dto.setAddress3(entity.getTransaction().getAddress3());
            dto.setTidStat(entity.getTransaction().getTidStat());
            dto.setCreatedAt(entity.getCreatedAt());
            dto.setUpdatedAt(entity.getTransaction().getUpdatedAt());
            dto.setPdName(entity.getProduct().getPdName());
            return dto;
        });
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OrderItemDTO> getOrderItems(String userEmail){

        Member member = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

        List<OrderItem> orderItems = orderItemRepository.findByMember(member);

        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems){
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setPrice(orderItem.getPrice());
            orderItemDTO.setAddress1(orderItem.getTransaction().getAddress1());
            orderItemDTO.setAddress2(orderItem.getTransaction().getAddress2());
            orderItemDTO.setAddress3(orderItem.getTransaction().getAddress3());
            orderItemDTO.setTidStat(orderItem.getTransaction().getTidStat());
            orderItemDTO.setUpdatedAt(orderItem.getTransaction().getUpdatedAt());
            orderItemDTO.setPdName(orderItem.getProduct().getPdName());

            orderItemDTOS.add(orderItemDTO);
            break;
        }
        return orderItemDTOS;
    }

    @org.springframework.transaction.annotation.Transactional
    public void updateOrderItem(Long orderItemId, String stat){
        log.info("orderitemId?: {}", orderItemId);
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);

        if(!orderItemOptional.isPresent()){
            throw new RuntimeException("주문이 없습니다.");
        }

        OrderItem orderItem = orderItemOptional.get();
        Long tid = orderItem.getTransaction().getTid();

        Optional<Transaction> transactionOptional = transactionRepository.findById(tid);

        Transaction transaction = transactionOptional.get();
        transaction.setTidStat(stat);

        transactionRepository.save(transaction);
    }
}
