package com.steve.blog.web.admin;

import com.steve.blog.pojo.Tag;
import com.steve.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model){
        Page<Tag> tagPage = tagService.listTags(pageable);
        model.addAttribute("page", tagPage);
        return "/admin/tags";
    }
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag", new Tag());
        return "/admin/tags-input";
    }
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        // 判断是否重复添加
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1 != null){
            result.rejectValue("name", "nameError", "该分类名称已经重复");
            // 在当前页面重复输入正确不重复的标签
        }
        if (result.hasErrors()){
            return "/admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null){
            attributes.addFlashAttribute("message", "标签新增失败");
        } else {
            attributes.addFlashAttribute("message", "标签新增成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable("id") Long id, Model model){
        Tag tag = tagService.getTagById(id);
        model.addAttribute("tag", tag);
        return "/admin/tags-input";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes){
        // 判断是否重复添加
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1 != null){
            result.rejectValue("name", "nameError", "该分类名称已经重复");
            // 在当前页面重复输入正确不重复的标签
        }
        if (result.hasErrors()){
            return "/admin/tags-input";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t == null){
            attributes.addFlashAttribute("message", "标签更新失败");
        } else {
            attributes.addFlashAttribute("message", "标签更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "标签删除成功");
        return "redirect:/admin/tags";
    }


}
