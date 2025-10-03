package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.UserService;

//@Tag(name="View", description="Endpoints for rendering views")
@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    /* Home page */
    //@Operation(summary = "Home page", description = "Display the home page with a selection of events")
    /* 
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("events", eventService.findThreeEvents());
        return "index";
    }
*/
    /* Event creation form */
    //@Operation(summary = "New event form", description = "Display the form for creating a new event")
    /*/
    @GetMapping("/admin/newEvent")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("sport", SportEnum.values());
        model.addAttribute("city", CityEnum.values());
        return "newEvent";
    }
*/
    /* Event creation */
/*
    //@Operation(summary = "Create event", description = "Handle the submission of the new event form")
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
*/
    /* Shopping cart page */
    //@Operation(summary = "Shopping cart", description = "Display the shopping cart page")
    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        return "shoppingCart";
    }

    @GetMapping("/payment")
    public String payment(Model model) {
        return "payment";
    }
}
