package com.example.controllers;

import com.example.entity.User;
import com.example.exceptions.ProfileException;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongyudeng
 */
@Controller
@RequestMapping("/User")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute("NOW_USER");
        Map<String, List> model = new HashMap<>();
        if (user != null) {
            List<User> users = new ArrayList<>();
            model.put("user", users);
        }
        return new ModelAndView("index.html", model);
    }

    @GetMapping("/login")
    public ModelAndView login(HttpSession session) {
        User user = (User) session.getAttribute("NOW_USER");
        if (user != null) {
            return new ModelAndView("redirect:/User/profile");
        }
        return new ModelAndView("login.html");
    }

    @PostMapping("/login")
    public ModelAndView doLogin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session) {
        try {
            User user = userService.login(username, password);
            session.setAttribute("NOW_USER", user);
        } catch (RuntimeException runtimeException) {
            return new ModelAndView("redirect:/User/login", Map.of("error", runtimeException.getMessage()));
        }
        return new ModelAndView("redirect:/User/profile");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("gender") String gender,
                                   HttpSession session) {
        try {
            userService.register(username, password, gender);
        } catch (RuntimeException runtimeException) {
            return new ModelAndView("register.html", Map.of("error", runtimeException.getMessage()));
        }
        return new ModelAndView("redirect:/User/login");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.setAttribute("NOW_USER",null);
        return new ModelAndView("redirect:/User/login");
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) {
        User user = (User) session.getAttribute("NOW_USER");
        if (user == null) {
            throw new ProfileException("no User");
        }
        return new ModelAndView("profile.html", Map.of("user", user));
    }
    @ExceptionHandler(ProfileException.class)
    private ModelAndView handleProfileException(){
        return new ModelAndView("redirect:/User/login");
    }
}
