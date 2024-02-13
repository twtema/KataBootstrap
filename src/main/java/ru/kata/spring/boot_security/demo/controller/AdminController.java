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
import java.security.Principal;
import java.util.Set;

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

    @GetMapping("")
    public String adminPage(Model model, Principal principal) {
        User user = userService.getUserByName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleService.getListRoles());
        return "admin";
    }


    @GetMapping("/edit/{id}")
    public String editUser ( @PathVariable("id") long id, Model model){
        model.addAttribute("editUser", userService.getUserById(id));
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleService.getListRoles());
        return "/admin";
    }


    @PatchMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, @ModelAttribute("editUser") @Valid User updateUser, BindingResult bindingResult,
                         @RequestParam(value = "roles", required = false) Set<Long> roleIds) {

        if (bindingResult.hasErrors())
            return "/admin";

//        userService.editUserAndHisRoles(id, updateUser, roleIds);

        userService.editUser(id, updateUser);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id,
                             @ModelAttribute("deleteUser") User deleteUser,
                             Model model) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("deleteUser", userService.getUserById(id));
        return "/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String registrationPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleService.getListRoles());
        return "/admin";
    }

    @PostMapping("/add")
    public String performRegistration(@ModelAttribute("person") @Valid User user, BindingResult bindingResult,
                                      @RequestParam(value = "roles", required = false) Set<Integer> roleIds) {

        if (bindingResult.hasErrors())
            return "/admin";

        userService.addUser(user);
        return "redirect:/admin";
    }


}


