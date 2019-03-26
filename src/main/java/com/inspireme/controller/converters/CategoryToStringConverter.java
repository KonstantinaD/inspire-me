package com.inspireme.controller.converters;

import com.inspireme.model.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToStringConverter implements Converter<Category, String> {

    @Override
    public String convert(Category category) {
        return String.valueOf(category.getCategoryId());
    }
}