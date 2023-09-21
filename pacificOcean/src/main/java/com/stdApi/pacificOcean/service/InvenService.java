package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Inventory;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.repository.InvenRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<Inventory> invenOpt = invenRepository.findBypdNo(product);

        if(!invenOpt.isPresent()){
            throw new RuntimeException("inventory 정보없음");
        }

        return true;
    };

    // 상품의 재고 감소
    public void decreaseInventory(Inventory inventory, int quantity) {
        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("재고 부족");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);

        // 데이터베이스에 재고 업데이트
        invenRepository.save(inventory);
    }

    // 상품의 재고 추가
    public void increaseInventory(Product product, int quantity){

        Optional<Inventory> invenOpt = invenRepository.findBypdNo(product);

        if(!invenOpt.isPresent()){
            throw new RuntimeException("inventory 정보없음");
        }
        Inventory inventory = invenOpt.get();
        inventory.setQuantity(inventory.getQuantity()+quantity);

        // 데이터베이스에 재고 업데이트
        invenRepository.save(inventory);
    };


}
