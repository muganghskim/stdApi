package com.stdApi.pacificOcean.repository;

import com.stdApi.pacificOcean.model.Category;
import com.stdApi.pacificOcean.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<Subcategory, Long> {
//    Optional<Subcategory> findBySubcategoryName(String subCategoryName);
@Query("SELECT s FROM Subcategory s WHERE s.subcategoryName = :subcategoryName")
Optional<Subcategory> findBySubcategoryName(String subcategoryName);
}
