package com.pchab.JoParis2024.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name="Ticket", description="Endpoints for managing tickets")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    
}
