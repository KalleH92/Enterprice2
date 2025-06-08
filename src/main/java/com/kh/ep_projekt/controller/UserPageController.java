package com.kh.ep_projekt.controller;

import com.kh.ep_projekt.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.kh.ep_projekt.model.User;

import java.security.Principal;


@Controller
@RequestMapping("/users")
public class UserPageController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/users/login";
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // pekar på templates/register.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // pekar på templates/login.html
    }

    @PostMapping("/register-form")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.registerUser(user.getName(), user.getEmail(), user.getPasswordHash());
            return "redirect:/users/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // Du kan lägga till felmeddelanden här senare
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Tar bort användardata
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userEmail", principal.getName());
        } else {
            model.addAttribute("userEmail", "Okänd");
        }
        return "home";
    }

    @PostMapping("/delete")
    public String deleteUser(Principal principal, HttpSession session) {
        if (principal !=null) {
            userService.deleteUserByEmail(principal.getName());
            session.invalidate();
        }
        return "redirect:/users/login";
    }
}