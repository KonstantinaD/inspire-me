package com.inspireme.presentationlayer.notfoundexceptions;

public class NoArticlesFoundInCategoryException extends RuntimeException{
    public NoArticlesFoundInCategoryException(Long categoryId) {
        super("No articles found in category " + categoryId); }
}
