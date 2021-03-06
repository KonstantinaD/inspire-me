package com.inspireme.service;

import com.inspireme.model.Tag;

import java.util.List;

public interface TagService {

    Tag retrieveTag(Long tagId);

    List<Tag> retrieveAllTags();

    Tag saveTag(Tag tag);

    Tag updateTag(Tag newTag, Long tagId);

    void deleteTag (Long tagId);
}
