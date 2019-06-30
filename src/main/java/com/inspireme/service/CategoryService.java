package com.inspireme.service;

import com.inspireme.model.Category;

import java.util.List;

public interface CategoryService {

    Category retrieveCategory(Long categoryId);

    List<Category> retrieveAllCategories();
}
