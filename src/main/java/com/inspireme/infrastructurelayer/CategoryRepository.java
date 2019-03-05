package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{
}
