package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AdminController(UserService userService1, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.userService = userService1;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping("/user")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }

    @GetMapping("/new")
    public String showAddUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", roleService.getListRoles());
        return "new";
    }

    @PostMapping("/user")
    public String createUser(
            @ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "new";
        }
        userService.addUser(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/user/{id}/edit")
    public String showEditUser(
            Model model, @PathVariable("id") Long id, Model roles) {
        roles.addAttribute("listRoles", roleService.getListRoles());
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PostMapping("/user/{id}")
    public String editUser(@ModelAttribute("user") @Valid User user,
                           @PathVariable("id") Long id, BindingResult result) {
        if (result.hasErrors()) {
            return "edit";
        }
        userService.editUser(id, user);
        return "redirect:/admin/user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return "redirect:/admin/user";
    }

}