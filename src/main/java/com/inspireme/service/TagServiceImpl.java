package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import com.inspireme.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<Tag> retrieveTag(Long tagId) {
        return tagRepository.findById(tagId);
    }

    @Override
    public List<Tag> retrieveAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> retrieveAllTagsPerArticle(Article article) {
        return tagRepository.findByArticle(article);
    }
}
