package com.kh.ep_projekt.controller;

import com.kh.ep_projekt.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/users")
public class UserPageController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // pekar på templates/register.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // pekar på templates/login.html
    }

    @PostMapping("/register-form")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           HttpServletResponse response) {
        try {
            userService.registerUser(name, email, password);
            return "redirect:/users/login";
        } catch (RuntimeException e) {
            return "register"; // Du kan lägga till felmeddelanden här senare
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Tar bort användardata
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String showHomePage(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        String userEmail = null;
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userEmail".equals(cookie.getName())) {
                    userEmail = cookie.getValue();
                    break;
                }
            }
        }
        model.addAttribute("userEmail", userEmail);
        return "home"; // pekar på templates/home.html
    }
}