package com.steve.blog.service;

import com.steve.blog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);
    void deleteTag(Long id);
    Tag updateTag(Long id, Tag tag);
    Tag getTagById(Long id);
    Tag getTagByName(String name);
    Page<Tag> listTags(Pageable pageable);
    List<Tag> listTag();
    List<Tag> listTag(String ids);
}