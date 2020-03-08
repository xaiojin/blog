package com.blog.web.admin;

import com.blog.po.Blog;
import com.blog.po.User;
import com.blog.service.BlogService;
import com.blog.service.TagService;
import com.blog.service.TypeService;
import com.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
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

    //得到用户信息
    public void getsession(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
    }

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable, BlogQuery blog, Model model,HttpSession session){
        getsession(model,session);

        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model,HttpSession session) {
        getsession(model,session);

        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }
    //跳到新增页面
    @GetMapping("blogs/input")
    public String input(Model model,HttpSession session){
        getsession(model,session);
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    //初始化
    public void setTypeAndTag(Model model){
        //初始化分类
        model.addAttribute("types",typeService.listType());
        //初始化标签
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("blogs/{id}/input")
    public String input(Model model,@PathVariable Long id,HttpSession session){
        getsession(model,session);
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }


    //保存博客
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes,HttpSession session){
        //设置用户
        blog.setUser((User) session.getAttribute("user"));
        //分类
        blog.setType(typeService.getType(blog.getType().getId()));
        //标签
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b;
        if (blog.getId() == null){
            //新增的话直接保存博客
            b = blogService.saveBlog(blog);
        }else {
            b = blogService.updateBlog(blog.getId(),blog);
        }

        if (b==null){
            attributes.addFlashAttribute("message","新增失败");
        }else {
            attributes.addFlashAttribute("message","新增成功");
        }
        return REDIRECT_LIST;
    }

    //删除博客
    @GetMapping("blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes,HttpSession session,Model model){
        getsession(model,session);
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;

    }
}
