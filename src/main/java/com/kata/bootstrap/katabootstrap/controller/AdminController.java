package com.kata.bootstrap.katabootstrap.controller;

import com.kata.bootstrap.katabootstrap.entity.Role;
import com.kata.bootstrap.katabootstrap.entity.User;
import com.kata.bootstrap.katabootstrap.service.RoleServiceImpl;
import com.kata.bootstrap.katabootstrap.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
//@Transactional
public class AdminController {

    private UserDetailsServiceImpl userDetailsService;
    private RoleServiceImpl roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserDetailsServiceImpl userDetailsService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    //@GetMapping("/admin")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("allUsers", userDetailsService.getAllUsers());
        return "admin";
    }

//    @GetMapping("/user")
//    public String userPage(Model model, Principal principal) {
//        User user = userDetailsService.getUserByName(principal.getName());
//        model.addAttribute("user", user);
//
//        return "user_user";
//    }


    @GetMapping("/admin/user/{id}")
    //@RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    public String userPageByID(@PathVariable("id") Long id, Model model) {
        User user = userDetailsService.getUser(id);
        user.getRoles().size();
        model.addAttribute("user", user);

        return "user";
    }

    //Добавление нового пользователя
    //@GetMapping("/admin/new")
    @RequestMapping(value = "/admin/new", method = RequestMethod.GET)
    public String newUser(@ModelAttribute("newUser") User user) {
        return "new";
    }


    @PostMapping()
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam(value = "nameRoles", required = false) String[] nameRoles) {

        //если одно из полей пустое не создаем пользователя
        if (user.getName().isEmpty() | user.getPassword().isEmpty() | user.getEmail().isEmpty()) {
            return "error_add_user";
        }

        user.getRoles();
        //если пользователь с таким именем существует возвращаемся в админку
        if (userDetailsService.getUserByName(user.getName()) != null) {
            return "redirect:/admin";
        }

        //шифрование пароля введенного при регистрации
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //создание сета с ролями
        Set<Role> roleSet = new HashSet<>();

        //получаем наши чекбоксы из страницы регистрации, если чтото есть, вставляем в набор ролей
        if (nameRoles != null) {
            for (String role : nameRoles) {
                roleSet.add(roleService.getRoleByName(role));
            }
        }

        //установка ролей юзеру
        user.setRoles(roleSet);

        //запись нового юзера в БД
        userDetailsService.addUser(user);

        //возврат в админку
        return "redirect:/admin";
    }
    //окончание добавления пользователя

    //Удаление пользователя
    @DeleteMapping("/{id}")
    public String deletUser(@PathVariable("id") Long id) {

        userDetailsService.removeUserById(id);
        return "redirect:/admin";
    }

    //Обновление пользователя
    @Transactional
    @GetMapping("/admin/{id}/edit")
    //@RequestMapping(value = "/admin/{id}/edit", method = RequestMethod.PATCH)
    public String edit(@PathVariable("id") Long id, Model model) {
        User user = userDetailsService.getUser(id);

        List<String> roleAdmin = new ArrayList<>(2);
        List<String> roleTmp = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());

        if (roleTmp.contains("ROLE_USER")) {
            roleAdmin.add(0, "checked");
        } else {
            roleAdmin.add(0, null);
        }

        if (roleTmp.contains("ROLE_ADMIN")) {
            roleAdmin.add(1, "checked");
        } else {
            roleAdmin.add(1, null);
        }

        model.addAttribute("roleAdmin", roleAdmin);
        model.addAttribute("editUser", user);
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @PathVariable("id") Long id,
                             @RequestParam(value = "nameRoles", required = false) String[] nameRoles) {

        //ПАролли
        final String oldPassword = userDetailsService.getUser(id).getPassword();
//        if (!oldPassword.equals(user.getPassword())) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        }

        if (user.getPassword().isEmpty()) {
            user.setPassword(oldPassword);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }


        //отредактировать на случай если нет ролей
        Set<Role> roleSet = new HashSet<>();
        if (nameRoles != null) {
            for (String role : nameRoles) {
                roleSet.add(roleService.getRoleByName(role));
            }
        }

        user.setRoles(roleSet);

        userDetailsService.updateUser(id, user);
        return "redirect:/admin";
    }

}