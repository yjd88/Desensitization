package com.midou.sensitive.controller;

import com.midou.sensitive.aspect.annotation.EnableDesensitization;
import com.midou.sensitive.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Yang Jundong
 * @date: 2021/3/12 0012 15:26
 * @description: User控制器
 */
@Controller
public class UserController {


    @GetMapping("/user")
    @EnableDesensitization
    @ResponseBody
    public User getUser() {
        User user = new User();
        user.setName("张小三");
        user.setPhone("13261679033");
        user.setEmail("527197746@qq.com");
        user.setIDCard("140422199004151234");
        return user;
    }

}
