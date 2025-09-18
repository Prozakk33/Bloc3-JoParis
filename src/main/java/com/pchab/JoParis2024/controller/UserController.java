package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.security.payload.request.LoginRequest;
import com.pchab.JoParis2024.security.payload.request.SignUpRequest;
import com.pchab.JoParis2024.service.UserService;


@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    // Find a user by ID
    @GetMapping("/id/{id}")
    User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
 
    // Find a user by email
    @GetMapping("/email/{email}")
    User findByEmail(@PathVariable String email) {
        System.out.println("USERCONTROLLER - Searching for user with email: " + email);
        try {
            User user = userService.findUserByEmail(email);
            return user;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
    }

    @GetMapping("/signin")
    public String signIn(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "signin";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "signup";
    }
/*
    @PostMapping("/createUser")
    public String signUp(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            return "signup";
        }
        try {
            userService.createUser(user);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "L'email est déjà utilisé. Veuillez en choisir un autre.");
            return "signup";
        }
    }
*/
    @PostMapping("/error")
    public String emailError(@RequestParam String email, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("error", "Email is already in use!");
        return "redirect:/user/signup";
    }

    
    

}
