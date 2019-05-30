package com.inspireme.service.impl;

import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import com.inspireme.repository.TagRepository;
import com.inspireme.service.ArticleService;
import com.inspireme.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
//    private final ArticleService articleService;

    public TagServiceImpl(TagRepository tagRepository/*, ArticleService articleService*/) {
        this.tagRepository = tagRepository;
        //this.articleService = articleService;
    }

    @Override
    public Tag retrieveTag(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException(tagId, Tag.class));
    }

    @Override
    public List<Tag> retrieveAllTags() {
        return tagRepository.findAll();
    }

//    @Override
//    public List<Tag> retrieveAllTagsPerArticle(Long articleId) {
//        return tagRepository.findByArticle(articleService.retrieveArticle);
//    }

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Tag newTag, Long tagId) {

        Tag tagToUpdate = retrieveTag(tagId);

        tagToUpdate.setTagName(newTag.getTagName());

        return tagRepository.save(tagToUpdate);
    }

    @Override
    public void deleteTag(Long tagId) {
        tagRepository.delete(retrieveTag(tagId));
    }
}
