package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

    Tag retrieveTag(Long tagId);

    List<Tag> retrieveAllTags();

    List<Tag> retrieveAllTagsPerArticle(Article article);

//    Tag saveTag(Tag tag);
//
//    void deleteTag (Long TagId);
}
