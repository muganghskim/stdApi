package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Category;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.model.SimpleProductDTO;
import com.stdApi.pacificOcean.service.CartService;
import com.stdApi.pacificOcean.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@Api(value = "Category Controller", description = "카테고리 관련된 API")
public class CategoryController {
    @Data
    public static class RegiCategoryReq {
        private String categoryName;
        private String subCategoryName;
    }

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    @PostMapping("/registerCategory")
    @ApiOperation(value = "카테고리 등록", notes = "카테고리를 등록합니다.")
    public ResponseEntity<?> registerCategory(@RequestBody RegiCategoryReq regiCategoryReq) {
        log.info("categoryReq: {}", regiCategoryReq.getCategoryName());
        try{
            log.info("categoryReq: {}", regiCategoryReq.getCategoryName());
            categoryService.saveCategory(regiCategoryReq.getCategoryName(), regiCategoryReq.getSubCategoryName());
            return ResponseEntity.ok("200");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
