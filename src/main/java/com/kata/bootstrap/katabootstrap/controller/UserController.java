package com.kata.bootstrap.katabootstrap.controller;

import com.kata.bootstrap.katabootstrap.entity.User;
import com.kata.bootstrap.katabootstrap.service.RoleServiceImpl;
import com.kata.bootstrap.katabootstrap.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    private UserDetailsServiceImpl userDetailsService;
    private RoleServiceImpl roleService;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService, RoleServiceImpl roleService) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        User user = userDetailsService.getUserByName(principal.getName());
        model.addAttribute("user", user);

        return "user";
    }
}
