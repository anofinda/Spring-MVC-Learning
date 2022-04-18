package com.example.controllers;

import com.example.entities.User;
import com.example.exceptions.ProfileException;
import com.example.jsonBeans.MailMessage;
import com.example.services.MailMessageService;
import com.example.services.MailService;
import com.example.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public static final String NOW_USER = "__user__";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    MailMessageService mailMessageService;

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
            mailMessageService.sendMailMessage(MailMessage.login(user));
            session.setAttribute("NOW_USER", user);
        } catch (RuntimeException runtimeException) {
            return new ModelAndView("redirect:/User/login", Map.of("error", runtimeException.getMessage()));
        } catch (JsonProcessingException jsonException) {
            logger.info("send login mail failed.");
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
                                   @RequestParam("email") String email,
                                   @RequestParam("gender") String gender,
                                   HttpSession session) {
        try {
            userService.register(username, password, email, gender);
            User user = userService.getUserByName(username);
            logger.info("user Email: {}", user.getEmail());
            mailMessageService.sendMailMessage(MailMessage.registration(user));
        } catch (RuntimeException runtimeException) {
            return new ModelAndView("register.html", Map.of("error", runtimeException.getMessage()));
        } catch (JsonProcessingException jsonException) {
            logger.info("send registration mail failed.");
        }
        return new ModelAndView("redirect:/User/login");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.setAttribute("NOW_USER", null);
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
    private ModelAndView handleProfileException() {
        return new ModelAndView("redirect:/User/login");
    }
}
