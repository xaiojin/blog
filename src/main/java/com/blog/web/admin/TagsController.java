package com.blog.web.admin;

import com.blog.dao.TagRepository;
import com.blog.po.Tag;
import com.blog.po.User;
import com.blog.service.TagService;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagsController {

    //注入
    @Autowired
    private TagService tagService;

    //    分页查询
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                   Pageable pageable, HttpSession session, Model model){
        Page<Tag> tags = tagService.listType(pageable);
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("page",tags);

        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model,HttpSession session){
        model.addAttribute("tag",new Tag());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes,HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);

        Tag t1 = tagService.getTagByName(tag.getName());
        if (t1!=null){
            result.rejectValue("name","nameError","不能添加重复的标签");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }
        Tag tag1 = tagService.saveTag(tag);
        if (tag1==null){
            attributes.addFlashAttribute("message","新增失败");
        }else {
            attributes.addFlashAttribute("message","新增成功");
        }

        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String post(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes,HttpSession session,Model model){

        Tag t1 = tagService.getTagByName(tag.getName());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        if (t1!=null){
            result.rejectValue("name","nameError","不能添加重复的标签");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }
        Tag tag1 = tagService.updateTag(tag,id);
        if (tag1==null){
            attributes.addFlashAttribute("message","更新失败");
        }else {
            attributes.addFlashAttribute("message","更新成功");
        }

        return "redirect:/admin/tags";
    }

    //跳到修改页面
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id,Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    //删除
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes,HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        tagService.delete(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}
