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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class EventViewController {

    @Autowired
    private EventService eventService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "index";
    }

    @GetMapping("/allEvents")
    public String allEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "allEvents";  
    }
        
    @GetMapping("/newEvent)")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("sport", SportEnum.values());
        model.addAttribute("city", CityEnum.values());
        return "newEvent";
    }

    @PostMapping
    public String createEvent(@Valid @ModelAttribute("event") Event event  , BindingResult result) {
        if (result.hasErrors()) {
            return "newEvent";
        }
        eventService.createEvent(event); 
        return "redirect:/allEvents";
       }

}
