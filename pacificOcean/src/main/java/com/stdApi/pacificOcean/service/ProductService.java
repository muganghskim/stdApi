package com.stdApi.pacificOcean.service;



import com.stdApi.pacificOcean.model.*;
import com.stdApi.pacificOcean.repository.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final InvenRepository invenRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, InvenRepository invenRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.invenRepository = invenRepository;
    }

    // 상품 등록
    public Product registerProduct(String userEmail, String pdName, String categoryName, String subCategoryName, String pdDetail, int pdPrice,
                                   String pdStat, String pdSize, String pdImg) {

        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
        Optional<Subcategory> subcategoryOpt = subCategoryRepository.findBySubcategoryName(subCategoryName);

        Subcategory subcategory = subcategoryOpt.get();

        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        if (!categoryOpt.isPresent()){
            throw new RuntimeException("카테고리를 찾을 수 없습니다.");
        }
        if (!subcategoryOpt.isPresent()){
            throw new RuntimeException("서브 카테고리를 찾을 수 없습니다.");
        }

        if (pdPrice < 0) {
            throw new IllegalArgumentException("상품 가격은 음수일 수 없습니다.");
        }

        Product product = Product.builder()
                .pdName(pdName)
                .category(categoryOpt.get())
                .subcategory(subcategoryOpt.get())
                .pdDetail(pdDetail)
                .member(memberOpt.get())
                .pdPrice(pdPrice)
                .pdStat(pdStat)
                .pdSize(pdSize)
                .pdImg(pdImg)
                .build();



        return productRepository.save(product);

    }

//    // 상품 전체 조회
//    public List<Product> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        return products;
//    }

    public List<SimpleProductDTO> getSimpleProducts() {
        List<Product> products = productRepository.findAll(); // Assuming you have a repository
        return products.stream()
                .map(this::convertToSimpleProductDTO)
                .collect(Collectors.toList());
    }

    private SimpleProductDTO convertToSimpleProductDTO(Product product) {
        SimpleProductDTO dto = new SimpleProductDTO();
        dto.setPdNo(product.getPdNo());
        dto.setPdName(product.getPdName());
        dto.setPdImg(product.getPdImg());
        dto.setPdPrice(product.getPdPrice());
        // Set other fields as necessary
        return dto;
    }

    // 상품 하나 조회(디테일 페이지)
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long pdNo) {

        Optional<Product> productDetail = productRepository.findById(pdNo);

        Optional<Inventory> inventory = invenRepository.findByProduct(productDetail.get());

        if (!inventory.isPresent()) {
            throw new RuntimeException("인벤토리를 찾을 수 없습니다.");
        }

        if (productDetail.isPresent()) {
            Product product = productDetail.get();
            Category category = product.getCategory();
            Subcategory subcategory = product.getSubcategory();
            String categoryId = category.getCategoryName();
            String subcategoryId = subcategory.getSubcategoryName();
            // product 사용
            ProductDTO dto = new ProductDTO();
            dto.setPdNo(product.getPdNo());
            dto.setPdImg(product.getPdImg());
            dto.setCategoryId(categoryId);
            dto.setPdPrice(product.getPdPrice());
            dto.setPdDetail(product.getPdDetail());
            dto.setSubcategoryId(subcategoryId);
            dto.setPdSize(product.getPdSize());
            dto.setPdStat(product.getPdStat());
            dto.setPdName(product.getPdName());
            dto.setPdQuantity(inventory.get().getQuantity());

            return dto;
        }
        return null;

    }

    // 상품 검색
    public List<Product> getProductByName(String pdName) {
        List<Product> products = productRepository.findByPdName(pdName);
        return products;
    }

}
