package com.kata.bootstrap.katabootstrap.controller;

import com.kata.bootstrap.katabootstrap.entity.Role;
import com.kata.bootstrap.katabootstrap.entity.User;
import com.kata.bootstrap.katabootstrap.service.RoleService;
import com.kata.bootstrap.katabootstrap.service.RoleServiceImpl;
import com.kata.bootstrap.katabootstrap.service.UserDetailsServiceImpl;
import com.kata.bootstrap.katabootstrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@Transactional
public class AdminController {

    private UserService userDetailsService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserDetailsServiceImpl userDetailsService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String userList(Model model, Principal principal) {
        model.addAttribute("users", userDetailsService.getAllUsers());
        model.addAttribute("current", userDetailsService.getUserByName(principal.getName()));
        model.addAttribute("newUser", new User());
        model.addAttribute("all_roles", roleService.allRoles());
        return "admin";
    }

    //Создание нового пользователя
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("role_select") Long[] roleIds,
                           @RequestParam("password") String password) {

        Set<Role> roles = new HashSet<>();
        for (Long id : roleIds) {
            roles.add(roleService.getRoleById(id));
        }
        user.setRoles(roles);

        String bCryptPassword = password.isEmpty() ?
                userDetailsService.getUser(user.getId()).getPassword() :
                passwordEncoder.encode(password);

        user.setPassword(bCryptPassword);
        userDetailsService.addUser(user);
        return "redirect:/admin";
    }

    //Удаление пользователя
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userDetailsService.removeUserById(id);
        return "redirect:/admin";
    }

    //Обновление пользователя
    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("role_select") Long[] roleIds,
                             @RequestParam("password") String password) {

        String bCryptPassword = password.isEmpty() ?
                userDetailsService.getUser(user.getId()).getPassword() :
                passwordEncoder.encode(password);

        //отредактировать на случай если нет ролей
        Set<Role> roles = new HashSet<>();
        for (Long id : roleIds) {
            roles.add(roleService.getRoleById(id));
        }
        user.setRoles(roles);

        user.setPassword(bCryptPassword);
        userDetailsService.updateUser(user.getId(), user);
        return "redirect:/admin";
    }

}