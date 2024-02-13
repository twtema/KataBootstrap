package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DBInit {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DBInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void initializedDataBase() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);
        Set<Role> adminRoleSet = new HashSet<>();
        Set<Role> userRoleSet = new HashSet<>();
        System.out.println(adminRoleSet.add(roleAdmin));
        System.out.println(adminRoleSet.add(roleUser));
        System.out.println(userRoleSet.add(roleUser));
        User admin = new User(
                "admin1", "admin1", "Ivan", "Ivanov", "ivanov@mail.com", 18 );
        User admin2 = new User("admin2", "admin2", "Oleg", "Olegov", "olegov@mail.com", 15);

        User user1 = new User("user1", "user1", "Alexander", "Alexandrov", "alexandr@Mail.com", 20);
        User user2 = new User(
                "user2", "user2", "Maria", "Ivanova", "ivanova@mail.com", 25);
        admin.setRoles(adminRoleSet);
        admin2.setRoles(adminRoleSet);
        user1.setRoles(userRoleSet);
        user2.setRoles(userRoleSet);
        userService.addUser(admin);
        userService.addUser(admin2);
        userService.addUser(user1);
        userService.addUser(user2);
    }
}
