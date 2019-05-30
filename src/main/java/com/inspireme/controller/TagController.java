package com.inspireme.controller;

import com.inspireme.controller.assemblers.TagResourceAssembler;
import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import com.inspireme.service.TagService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/tags")
public class TagController {

    private final TagService tagService;
    private final TagResourceAssembler tagAssembler;

    public TagController(TagService tagService, TagResourceAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping
    public Resources<?> getAllTags() {

        List<Tag> tags = tagService.retrieveAllTags();

        if (!tags.isEmpty()) {
            List<Resource<Tag>> tagResources = tagService.retrieveAllTags().stream()
                    .map(tagAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(tagResources,
                    linkTo(methodOn(TagController.class).getAllTags()).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListTagWrapper()),
                linkTo(methodOn(TagController.class).getAllTags()).withSelfRel());
    }


    @GetMapping("/{tagId}")
    public Resource<Tag> getTag(@PathVariable Long tagId) {

        return tagAssembler.toResource(tagService.retrieveTag(tagId));
    }

    @PostMapping
    public ResponseEntity<?> createNewTag(@RequestBody @Valid Tag newTag) throws URISyntaxException {

        Resource<Tag> tagResource = tagAssembler.toResource(tagService.saveTag(newTag));

        return ResponseEntity
                .created(new URI(tagResource.getId().expand().getHref()))
                .body(tagResource);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<?> editTag(@RequestBody @Valid Tag newTag, @PathVariable Long tagId) throws URISyntaxException {

        Tag updatedTag = tagService.updateTag(newTag, tagId);

        Resource<Tag> tagResource = tagAssembler.toResource(updatedTag);

        return ResponseEntity
                .created(new URI(tagResource.getId().expand().getHref()))
                .body(tagResource);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable Long tagId) {

        tagService.deleteTag(tagId);

        return ResponseEntity.noContent().build();
    }

    protected static EmbeddedWrapper getEmptyListTagWrapper(){
        return new EmbeddedWrappers(false).emptyCollectionOf(Tag.class);
    }
}


