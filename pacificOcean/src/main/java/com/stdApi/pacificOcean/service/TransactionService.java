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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;
    private final RevenueService revenueService;
    private final InvenService invenService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, OrderItemRepository orderItemRepository, RevenueService revenueService, InvenService invenService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderItemRepository = orderItemRepository;
        this.revenueService = revenueService;
        this.invenService = invenService;
    }

    @Transactional
    public void createTransaction(String userEmail, Long deliveryId, String rcvName, String rcvPhn, String tidStat, String paymentMethod, List<Long> orderItemIds) {
        Member member = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid member id"));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new IllegalArgumentException("Invalid delivery id"));

        com.stdApi.pacificOcean.model.Transaction transaction = Transaction.builder()
                .member(member)
                .address1(delivery.getUserAddress1())
                .address2(delivery.getUserAddress2())
                .address3(delivery.getUserAddress3())
                .rcvName(rcvName)
                .rcvPhn(rcvPhn)
                .tidStat(tidStat)
                .paymentMethod(paymentMethod)
                .build();

        List<Map<String, String>> pdDataList = new ArrayList<>();


        for(Long orderItemId : orderItemIds) {
            OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new IllegalArgumentException("Invalid order item id"));
            transaction.addOrderItem(orderItem);

            Map<String, String> pdData = new HashMap<>();
            pdData.put("pdNo", String.valueOf(orderItem.getProduct().getPdNo()));
            pdData.put("pdQuantity", String.valueOf(orderItem.getQuantity()));

            pdDataList.add(pdData);
        }

        // 한 번에 처리
        for (Map<String, String> pdData : pdDataList) {
            invenService.decreaseInventory(pdData.get("pdNo"), pdData.get("pdQuantity"));
        }



        transactionRepository.save(transaction);

        revenueService.createRevenue(transaction.getTid(), transaction.getTotalAmount());

//        return transaction;

    }
}
