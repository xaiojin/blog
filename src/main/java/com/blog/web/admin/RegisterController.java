package com.blog.web.admin;

import com.blog.dao.UserRepository;
import com.blog.po.User;
import com.blog.service.UserService;
import com.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/admin")
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/toregister")
    public String to_register(){
        return "admin/register";
    }

    @PostMapping("/register")
    public String register(User user,RedirectAttributes attributes){
        User username = userService.getUserByName(user.getUsername());
        if (username != null){
            attributes.addFlashAttribute("message","用户名已经存在！");
            return "redirect:/admin/toregister";
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        String password = user.getPassword();
        String code_password = MD5Utils.code(password);
        user.setPassword(code_password);
        userService.addUser(user);
        return "redirect:/admin";
    }
}
