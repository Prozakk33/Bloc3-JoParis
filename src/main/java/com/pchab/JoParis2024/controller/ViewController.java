package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.UserService;
import com.pchab.JoParis2024.pojo.User;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    /* Home page */
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "index";
    }

    /* List of all events */
    @GetMapping("/event/all")
    public String allEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "allEvents";  
    }
     
    /* Event creation form */
    @GetMapping("/admin/newEvent")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("sport", SportEnum.values());
        model.addAttribute("city", CityEnum.values());
        return "newEvent";
    }

    /* Event creation */
    @PostMapping("/admin/createEvent")
    public String createEvent(@Valid @ModelAttribute("event") Event event  , BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sport", SportEnum.values());
            model.addAttribute("city", CityEnum.values());
            return "newEvent";
        }
        eventService.createEvent(event); 
        return "redirect:/event/all";
       }
    
    /* Event detail */
    @GetMapping("/event/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findEventById(id));
        return "eventDetail";
    }

    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/user/signup")
    public String signUp(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }
        userService.createUser(user);
        return "redirect:/";
    }
    

}
