
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pchab.JoParis2024.controller.AdminController;
import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;
import com.pchab.JoParis2024.pojo.Event;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(controllers = AdminController.class)
public class AdminControllerUnitTest {

    @Autowired
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
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
        .build();
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
                .andExpect(jsonPath("$[0].totalRevenue").value(5000.0f));
                //.andDo(print());

    }

// ---------------------------------------------------------------------------------------

    
    @Test
    public void testCreateEvent() throws Exception {
        String mockToken = "mock-admin-token";
        Event mockEvent = new Event();
        //mockEvent.setId(2L); // ID de l'événement créé
        mockEvent.setTitle("New Event");
        mockEvent.setDescription("Description of the new event");
        mockEvent.setCity(CityEnum.LYON);
        mockEvent.setSport(SportEnum.BASKETBALL);
        mockEvent.setDate(LocalDateTime.parse("2024-08-01T15:00:00"));
        mockEvent.setStadium("Stadium 2");
        mockEvent.setCapacity(100);
        mockEvent.setPrice(75.0f);

        String requestBody = """
        {
        "title": "New Event",
        "description": "Description of the new event","city":"LYON","sport":"BASKETBALL","date":"2024-08-01T15:00:00","stadium":"Stadium 2","capacity":100,"price":75.0}
        "date": "2024-08-01T15:00:00",
        "sport": "BASKETBALL",
        "stadium": "Stadium 2",
        "city": "LYON",       
        "capacity": 100,
        "price": 75.0
        }
        """;

        when(eventService.createEvent(any(Event.class))).thenReturn(mockEvent); // ID de l'événement créé

        mockMvc.perform(post("/admin/createEvent")
                .header("Authorization", "Bearer " + mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
                //.andDo(print());
    }

    @Test
    public void testUpdateEvent_Success() throws Exception {
        String mockToken = "mock-admin-token";
        Event mockOldEvent = new Event();
        //mockEvent.setId(2L); // ID de l'événement créé
        mockOldEvent.setId(1L);
        mockOldEvent.setTitle("New Event");
        mockOldEvent.setDescription("Description of the new event");
        mockOldEvent.setCity(CityEnum.LYON);
        mockOldEvent.setSport(SportEnum.BASKETBALL);
        mockOldEvent.setDate(LocalDateTime.parse("2024-08-01T15:00:00"));
        mockOldEvent.setStadium("Stadium 2");
        mockOldEvent.setCapacity(100);
        mockOldEvent.setPrice(75.0f);

        Event mockNewEvent = new Event();
        mockNewEvent.setId(1L);
        mockNewEvent.setTitle("Updated Event");
        mockNewEvent.setDescription("Updated description of the event");
        mockNewEvent.setCity(CityEnum.MARSEILLE);
        mockNewEvent.setSport(SportEnum.JUDO);
        mockNewEvent.setDate(LocalDateTime.parse("2024-08-05T18:00:00"));
        mockNewEvent.setStadium("Updated Stadium");
        mockNewEvent.setCapacity(150);
        mockNewEvent.setPrice(90.0f);


        String requestBody = """
        {
        "eventId": 1,
        "eventTitle": "Updated Event",
        "eventDescription": "Updated description of the event",
        "eventCity": "MARSEILLE",
        "eventSport": "JUDO",
        "eventDate": "2024-08-05T18:00:00",
        "eventStadium": "Updated Stadium",
        "eventCapacity": 150,
        "eventPrice": 90.0
        }
        """;

        //when(eventService.updateEvent(eq(mockOldEvent), eq(mockOldEvent.getId()))).thenReturn(mockNewEvent); // ID de l'événement créé
        when(eventService.updateEvent(any(Event.class), anyLong())).thenReturn(mockNewEvent);

        mockMvc.perform(put("/admin/updateEvent")
                .header("Authorization", "Bearer " + mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());

    }
}