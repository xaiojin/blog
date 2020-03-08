package com.blog.service;

import com.blog.po.Tag;
import com.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TagService {

    //报存标签
    Tag saveTag(Tag tag);

    //删除标签
    void delete(Long id);

    //修改
    Tag updateTag(Tag tag,Long id);

    //id查询
    Tag getTag(Long id);
    //名字查询
    Tag getTagByName(String name);

    //分页查询
    Page<Tag> listType(Pageable pageable);
    //获取所有标签
    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);

}
