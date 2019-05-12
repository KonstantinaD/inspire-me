package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Tag;

import java.util.List;

public interface TagService {

    Tag retrieveTag(Long tagId);

    List<Tag> retrieveAllTags();

    List<Tag> retrieveAllTagsPerArticle(Article article);

//    void deleteTagPerArticle(Article article, Tag tag);
}
