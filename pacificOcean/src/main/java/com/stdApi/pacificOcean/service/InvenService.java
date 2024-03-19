package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.exception.InvenLackedException;
import com.stdApi.pacificOcean.model.*;
import com.stdApi.pacificOcean.repository.InvenRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class InvenService {

    private final InvenRepository invenRepository;

    @Autowired
    public InvenService(InvenRepository invenRepository) {
        this.invenRepository = invenRepository;

    }

    // 상품 재고 등록
    public Inventory createInventoryRecord(Product product, int quantity, String stockType){
        Inventory inventory = Inventory.builder()
                .quantity(quantity)
                .product(product)
                .stockType(stockType)
                .build();

        return invenRepository.save(inventory);
    }

    // 상품의 재고 확인
    public boolean checkInventory(Product product){
        Optional<Inventory> invenOpt = invenRepository.findByProduct(product);

        if(!invenOpt.isPresent()){
//            throw new RuntimeException("inventory 정보없음");
            return false;
        }

        return true;
    };

    // 상품의 재고 감소
    public void decreaseInventory(String pdNo, String quantity) {
        Inventory inventory = invenRepository.findProductNumber(Long.parseLong(pdNo));

        int newQuantity = inventory.getQuantity() - Integer.parseInt(quantity);
        if (newQuantity < 0) {
            throw new InvenLackedException("재고가 부족합니다.");
        }

        inventory.setQuantity(newQuantity);

        // 데이터베이스에 재고 업데이트
        invenRepository.save(inventory);
    }

    // 상품의 재고 추가
    public void increaseInventory(Product product, int quantity){

        Optional<Inventory> invenOpt = invenRepository.findByProduct(product);

        if(!invenOpt.isPresent()){
            throw new RuntimeException("inventory 정보없음");
        }
        Inventory inventory = invenOpt.get();
        inventory.setQuantity(inventory.getQuantity()+quantity);

        // 데이터베이스에 재고 업데이트
        invenRepository.save(inventory);
    };

    @Transactional(readOnly = true)
    public Page<InvenDTO> findAll(Pageable pageable) {
        Page<Inventory> page = invenRepository.findAll(pageable);
        return page.map(entity -> {
            InvenDTO dto = new InvenDTO();
            dto.setInvenId(entity.getInvenId());
            dto.setStockType(entity.getStockType());
            dto.setQuantity(entity.getQuantity());
            dto.setPdName(entity.getProduct().getPdName());
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }


}
