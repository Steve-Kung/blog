package com.steve.blog.web.admin;

import com.steve.blog.pojo.Blog;
import com.steve.blog.pojo.Tag;
import com.steve.blog.pojo.User;
import com.steve.blog.service.BlogService;
import com.steve.blog.service.TagService;
import com.steve.blog.service.TypeService;
import com.steve.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, BlogQuery blog){
        model.addAttribute("types", typeService.listType());
        Page<Blog> page = blogService.listBlogs(pageable, blog);
        model.addAttribute("page", page);
        return LIST;
    }
    @PostMapping("/blogs/search") // BlogQuery
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlogs(pageable, blog));
        return "admin/blogs :: blogList"; // 返回片段
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }


    private String tagIdsTotagIdsStr(String[] tagIds) {
        if (tagIds.length != 0) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (String tagId : tagIds) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tagId);
            }
            return ids.toString();
        } else {
            return null;
        }
    }


    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));

        // 下面为查询数据库中不含有的标签
        String tagIdsStr = blog.getTagIds();
        String[] tagIds = tagIdsStr.split(",");
        for (int i = 0; i<tagIds.length;i++){
            if(!("0123456789".contains(String.valueOf(tagIds[i].charAt(0))))){
                Tag tag = new Tag();
                tag.setName(tagIds[i]);
                tagService.saveTag(tag);
                Tag tag1 = tagService.getTagByName(tagIds[i]);
                tagIds[i] = String.valueOf(tag1.getId());
            }
        }
        tagIdsStr = tagIdsTotagIdsStr(tagIds);
        blog.setTagIds(tagIdsStr);
        // 上面为查询数据库中不含有的标签

        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b;

        if (blog.getId() == 0) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
