package com.stdApi.pacificOcean.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stdApi.pacificOcean.model.Inventory;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.model.ProductDTO;
import com.stdApi.pacificOcean.model.SimpleProductDTO;
import com.stdApi.pacificOcean.service.InvenService;
import com.stdApi.pacificOcean.service.ProductService;
import com.stdApi.pacificOcean.service.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "Product Controller", description = "상품 관련된 API")
public class ProductController {
    private final ProductService productService;

    private final StorageService storageService;

    private final InvenService invenService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 처리를 위한 ObjectMapper

    @Autowired
    public ProductController(ProductService productService, StorageService storageService, InvenService invenService) {
        this.productService = productService;
        this.storageService = storageService;
        this.invenService = invenService;
    }

    @Data
    public static class RegiProductReq {
        private String userEmail;
        private String pdName;
        private String categoryName;
        private String subCategoryName;
        private String pdDetail;
        private String pdPrice;
        private String pdStat;
        private String pdSize;
        private String pdQuantity; // 재고 수량 필드 추가
    }

//    //상품 등록
//    @PostMapping(value = "/registerProduct", consumes = "multipart/form-data")
//    @Transactional // 트랜잭션 관리
//    @ApiOperation(value = "상품 등록", notes = "상품을 등록합니다.")
//    public ResponseEntity<?> registerProduct(@RequestPart(value = "regiProductReq") RegiProductReq regiProductReq, @RequestPart(value = "file") MultipartFile file) {
//
//        log.debug("product data: {}", regiProductReq.getPdName());
//        try {
//
//
//            String storedFileUrl = storageService.storeFile(file);
//
//            // 상품 등록 요청에서 재고 수량 추출
//            int pdQuantity = Integer.parseInt(regiProductReq.getPdQuantity());
//
//            // 상품 등록
//            productService.registerProduct(regiProductReq.getUserEmail(), regiProductReq.getPdName(), regiProductReq.getCategoryName(), regiProductReq.getSubCategoryName() ,regiProductReq.getPdDetail(), Integer.parseInt(regiProductReq.getPdPrice()), regiProductReq.getPdStat(), regiProductReq.getPdSize(), storedFileUrl);
//
////            // 등록된 상품이면 수량 만큼 추가
////            if(invenService.checkInventory(registeredProduct)){
////                invenService.increaseInventory(registeredProduct, pdQuantity);
////            }else {
////                // 등록된 상품이 아니면 수량만큼 등록
////                Inventory createRecord = invenService.createInventoryRecord(registeredProduct, pdQuantity, "normal");
////            }
//
//            return ResponseEntity.ok("200");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @PostMapping("/admin/registerProduct")
    @Transactional // 트랜잭션 관리
    @ApiOperation(value = "상품 등록", notes = "상품을 등록합니다.")
    public ResponseEntity<?> registerProduct(@RequestPart String pdName,
                                             @RequestPart String userEmail,
                                             @RequestPart String categoryName,
                                             @RequestPart String subCategoryName,
                                             @RequestPart String pdDetail,
                                             @RequestPart String pdPrice,
                                             @RequestPart String pdStat,
                                             @RequestPart String pdSize,
                                             @RequestPart String pdQuantity,
                                             @RequestPart(value = "file") MultipartFile file) {


        try {
            log.info("product data: {}", pdName);

            String storedFileUrl = storageService.storeFile(file);

            // 상품 등록 요청에서 재고 수량 추출
            int quantity = Integer.parseInt(pdQuantity);

            // 상품 등록
            Product registeredProduct = productService.registerProduct(userEmail, pdName, categoryName, subCategoryName, pdDetail, Integer.parseInt(pdPrice), pdStat, pdSize, storedFileUrl);

//            // 등록된 상품이면 수량 만큼 추가
//            if(invenService.checkInventory(registeredProduct)){
//                invenService.increaseInventory(registeredProduct, quantity);
//            }else {
                // 등록된 상품이 아니면 수량만큼 등록
                Inventory createRecord = invenService.createInventoryRecord(registeredProduct, quantity, "normal");
//            }

            return ResponseEntity.ok("200");
        } catch (Exception e) {
            log.error("어디서 난겨",e);
            return ResponseEntity.badRequest().build();
        }
    }

    // 상품 전체 조회
    @GetMapping("/products")
    @ApiOperation(value = "상품 전체조회", notes = "상품을 전체조회합니다.")
    public ResponseEntity<List<SimpleProductDTO>> getAllProducts() {
        try{
            List<SimpleProductDTO> products = productService.getSimpleProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // 상품 하나 조회
    @GetMapping("/products/{productId}")
    @ApiOperation(value = "상품 하나조회", notes = "상품을 하나조회합니다.")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("productId") String productId) {
        try{
            ProductDTO product = productService.getProductById(Long.parseLong(productId));
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    // 상품 이름으로 검색
    @GetMapping("/productSearch")
    @ApiOperation(value = "상품 이름으로 검색", notes = "상품을 이름으로 검색합니다.")
    public ResponseEntity<List<Product>> getProductByName(@RequestParam("searchName") String searchName) {
        try{
            List<Product> searchProduct = productService.getProductByName(searchName);
            if (!searchProduct.isEmpty()) {
                return ResponseEntity.ok(searchProduct);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
