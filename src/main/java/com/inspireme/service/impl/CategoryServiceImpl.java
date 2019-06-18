package com.inspireme.service.impl;

import com.inspireme.model.Category;
import com.inspireme.repository.CategoryRepository;
import com.inspireme.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> retrieveAllCategories() {
        return categoryRepository.findAll();
    }
}
