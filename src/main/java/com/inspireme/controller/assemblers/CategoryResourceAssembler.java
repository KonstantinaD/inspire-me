package com.inspireme.controller.assemblers;

import com.inspireme.controller.CategoryController;
import com.inspireme.model.Category;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CategoryResourceAssembler implements ResourceAssembler<Category, Resource<Category>> {

    @Override
    public Resource<Category> toResource(Category category) {

        return new Resource<>(category,
                ControllerLinkBuilder.linkTo(methodOn(CategoryController.class).getCategory(category.getCategoryId()))
                        .withSelfRel(),
                ControllerLinkBuilder.linkTo(methodOn(CategoryController.class).getAllCategories())
                        .withRel("categories"));
    }
}
