package com.stdApi.pacificOcean.service;



import com.stdApi.pacificOcean.model.Category;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Product;
import com.stdApi.pacificOcean.repository.CategoryRepository;
import com.stdApi.pacificOcean.repository.ProductRepository;

import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // 상품 등록
    public Product registerProduct(String userEmail, String pdName,String categoryName, String pdDetail, int pdPrice,
                                   String pdStat, String pdSize, String pdImg) {

        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);

        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        if (!categoryOpt.isPresent()){
            throw new RuntimeException("카테고리를 찾을 수 없습니다.");
        }

        if (pdPrice < 0) {
            throw new IllegalArgumentException("상품 가격은 음수일 수 없습니다.");
        }

        Product product = Product.builder()
                .pdName(pdName)
                .category(categoryOpt.get())
                .pdDetail(pdDetail)
                .member(memberOpt.get())
                .pdPrice(pdPrice)
                .pdStat(pdStat)
                .pdSize(pdSize)
                .pdImg(pdImg)
                .build();

        return productRepository.save(product);
    }

    // 상품 전체 조회
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    // 상품 하나 조회(디테일 페이지)
    public Optional<Product> getProductById(Long pdNo) {
        Optional<Product> productDetail = productRepository.findById(pdNo);
        if (!productDetail.isPresent()) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }
        return productDetail;
    }

    // 상품 검색
    public List<Product> getProductByName(String pdName) {
        List<Product> products = productRepository.findByPdName(pdName);
        return products;
    }

}
