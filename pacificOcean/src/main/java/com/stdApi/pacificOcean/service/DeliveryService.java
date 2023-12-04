package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Delivery;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.repository.DeliveryRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final UserRepository userRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, UserRepository userRepository){
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
    }

    // 배송지 추가
    @Transactional
    public Delivery addDelivery(String userEmail, String userAddress1, String userAddress2, String userAddress3){
        Member member = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

        Delivery delivery = Delivery.builder()
                .member(member)
                .userAddress1(userAddress1)
                .userAddress2(userAddress2)
                .userAddress3(userAddress3)
                .build();

        return deliveryRepository.save(delivery);
    }

    // 배송지 정보 업데이트
    @Transactional
    public Delivery updateDelivery(Long deliverId, String userAddress1, String userAddress2, String userAddress3){
        Delivery delivery = deliveryRepository.findById(deliverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid delivery id"));

        delivery.setUserAddress1(userAddress1);
        delivery.setUserAddress2(userAddress2);
        delivery.setUserAddress3(userAddress3);

        return deliveryRepository.save(delivery);
    }

    // 배송지 정보 조회
    public Delivery getDelivery(Long deliverId){
        return deliveryRepository.findById(deliverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid delivery id"));
    }

    // 배송지 정보 삭제
    @Transactional
    public void deleteDelivery(Long deliverId){
        Delivery delivery = deliveryRepository.findById(deliverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid delivery id"));

        deliveryRepository.delete(delivery);
    }


}
