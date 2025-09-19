package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.UserService;

import jakarta.validation.Valid;


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
        model.addAttribute("events", eventService.findThreeEvents());
        return "index";
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
    
}
