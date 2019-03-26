package com.inspireme.controller.converters;

import com.inspireme.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Category convert(String id) {
        return entityManager.find(Category.class, Long.parseLong(id));
    }
}