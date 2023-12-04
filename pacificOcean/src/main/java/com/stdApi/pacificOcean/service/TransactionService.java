package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Delivery;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.OrderItem;
import com.stdApi.pacificOcean.model.Transaction;
import com.stdApi.pacificOcean.repository.DeliveryRepository;
import com.stdApi.pacificOcean.repository.OrderItemRepository;
import com.stdApi.pacificOcean.repository.TransactionRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, OrderItemRepository orderItemRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public com.stdApi.pacificOcean.model.Transaction createTransaction(String userEmail, Long deliveryId, String rcvName, String rcvPhn, String tidStat, String paymentMethod, List<Long> orderItemIds) {
        Member member = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid member id"));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new IllegalArgumentException("Invalid delivery id"));

        com.stdApi.pacificOcean.model.Transaction transaction = Transaction.builder()
                .member(member)
                .delivery(delivery)
                .rcvName(rcvName)
                .rcvPhn(rcvPhn)
                .tidStat(tidStat)
                .paymentMethod(paymentMethod)
                .build();

        
        for(Long orderItemId : orderItemIds) {
            OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new IllegalArgumentException("Invalid order item id"));
            transaction.addOrderItem(orderItem);
        }

        return transactionRepository.save(transaction);
    }
}
