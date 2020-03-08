package com.blog.web.admin;

import com.blog.po.Type;
import com.blog.po.User;
import com.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TypeController {

    @Autowired
    private TypeService typeService;

//    分页查询
    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    //新增页面
    @GetMapping("/types/input")
    public String input(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("type",new Type());
        return "admin/types-input";

    }

    //跳到修改页面
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    //新增标签
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes,Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);

        Type type1 = typeService.getTypeByName(type.getName());
        if (type1!=null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        Type t=typeService.saveType(type);
        if (t==null){
            attributes.addFlashAttribute("message","新增失败");
        }else {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
//        这里不能直接返回admin/types页面，因为返回这个页面的话没有经过page方法查询数据，所以得到的是一个没有数据的页面
    }

    //修改标签
    @PostMapping("/types/{id}")
    public String post(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes,Model model,HttpSession session){

        Type type1 = typeService.getTypeByName(type.getName());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        if (type1!=null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        Type t=typeService.updateType(id,type);
        if (t==null){
            attributes.addFlashAttribute("message","更新失败");
        }else {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
//        这里不能直接返回admin/types页面，因为返回这个页面的话没有经过page方法查询数据，所以得到的是一个没有数据的页面
    }

    //删除标签
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes,Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }

}
