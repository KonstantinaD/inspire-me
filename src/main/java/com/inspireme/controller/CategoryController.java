package com.inspireme.controller;

import com.inspireme.controller.assemblers.CategoryResourceAssembler;
import com.inspireme.model.Category;
import com.inspireme.service.CategoryService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryResourceAssembler categoryAssembler;

    public CategoryController(CategoryService categoryService, CategoryResourceAssembler categoryAssembler) {
        this.categoryService = categoryService;
        this.categoryAssembler = categoryAssembler;
    }

    @GetMapping
    public Resources<?> getAllCategories() {

        List<Category> categories = categoryService.retrieveAllCategories();

        if (!categories.isEmpty()) {
            List<Resource<Category>> categoryResources = categoryService.retrieveAllCategories().stream()
                    .map(categoryAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(categoryResources,
                    linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListCategoryWrapper()),
                linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel());
    }

    @GetMapping("/{categoryId}")
    public Resource<Category> getCategory(@PathVariable Long categoryId) {

        return categoryAssembler.toResource(categoryService.retrieveCategory(categoryId));
    }

    protected static EmbeddedWrapper getEmptyListCategoryWrapper(){
        return new EmbeddedWrappers(false).emptyCollectionOf(Category.class);
    }
}
