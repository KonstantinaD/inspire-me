package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;

interface CategoryRepository extends JpaRepository<Category, Long>{
}
