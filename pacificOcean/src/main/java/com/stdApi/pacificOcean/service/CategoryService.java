package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Category;
import com.stdApi.pacificOcean.model.Subcategory;
import com.stdApi.pacificOcean.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    public CategoryService(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Transactional
    public void saveCategory(String categoryName, String subCategoryName){
        Category category = Category.builder()
                .categoryName(categoryName)
                .build();

        log.info("카테고리 저장");
        categoryRepository.save(category);

        Subcategory subcategory = Subcategory.builder()
                .subcategoryName(subCategoryName)
//                .category(category)
                .build();

        log.info("서브 카테고리 저장");
        subCategoryRepository.save(subcategory);
    }
}
