
package com.pchab.JoParis2024;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pchab.JoParis2024.controller.AdminController;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;



@ExtendWith(MockitoExtension.class)
public class AdminControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetAllEvents() throws Exception {
        EventAdminResponse mockEvent1 = new EventAdminResponse();
        mockEvent1.setEventId(1L);
        mockEvent1.setEventTitle("Event 1");
        mockEvent1.setEventCity("PARIS");
        mockEvent1.setEventSport("FOOTBALL");
        mockEvent1.setEventDate(LocalDateTime.parse("2024-07-26T10:00:00"));
        mockEvent1.setEventStadium("Stadium 1");
        mockEvent1.setTotalTicketsSold(50L);
        mockEvent1.setTotalRevenue(5000.0f);

        List<EventAdminResponse> mockList = List.of(mockEvent1);
        String mockToken = "mock-admin-token";
        //when(jwtUtils.validateJwtToken(mockToken)).thenReturn(true); // Le token est valide
        //when(jwtUtils.getUserRoleFromJwtToken(mockToken)).thenReturn("ADMIN"); // Rôle ADMIN
        when(eventService.getAllEventsAdmin()).thenReturn(mockList);

        mockMvc.perform(get("/admin/events")
                .header("Authorization", "Bearer " + mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Ajout du corps de la requête
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value(1L))
                .andExpect(jsonPath("$[0].eventTitle").value("Event 1"))
                .andExpect(jsonPath("$[0].eventCity").value("PARIS"))
                .andExpect(jsonPath("$[0].eventSport").value("FOOTBALL"))
                .andExpect(jsonPath("$[0].eventDate").value("2024-07-26T10:00:00"))
                .andExpect(jsonPath("$[0].eventStadium").value("Stadium 1"))
                .andExpect(jsonPath("$[0].totalTicketsSold").value(50L))
                .andExpect(jsonPath("$[0].totalRevenue").value(5000.0f))
                .andDo(print());

    }

}