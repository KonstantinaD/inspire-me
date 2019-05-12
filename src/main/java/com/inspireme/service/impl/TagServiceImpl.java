package com.inspireme.service.impl;

import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import com.inspireme.repository.TagRepository;
import com.inspireme.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag retrieveTag(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException(tagId, Tag.class));
    }

    @Override
    public List<Tag> retrieveAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> retrieveAllTagsPerArticle(Article article) {
        return tagRepository.findByArticle(article);
    }

//    @Override
//    public void deleteTagPerArticle(Article article, Tag tag) {
//    tagRepository.deleteByArticle(article, tag);
//    }
}
