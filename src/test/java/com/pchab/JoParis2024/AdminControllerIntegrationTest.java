package com.pchab.JoParis2024;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.service.EventService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateEvent_Success() throws Exception {
        // Préparer les données
        Event event = new Event();
        event.setId(1L);
        event.setTitle("France - Italie");
        event.setDescription("Match d'ouverture du tournoi de football");
        event.setCity(CityEnum.PARIS);
        event.setDate(LocalDateTime.parse("2025-10-16T16:06:37.754"));
        event.setStadium("Stade de France");
        event.setSport(SportEnum.FOOTBALL);
        event.setCapacity(2000);
        event.setPrice(25.0f);

        // Convertir l'objet en JSON
        String eventJson = """
            {
            "eventTitle": "France - Italie",
            "eventDescription": "Match d'ouverture du tournoi de football",
            "eventDate": "2025-10-16T16:06:37.754",
            "eventSport": "FOOTBALL",
            "eventCity": "PARIS",
            "eventCapacity": 2000,
            "eventPrice": 25.0,
            "eventStadium": "Stade de France"
            }
        """;

        // Configurer le comportement du mock
        when(eventService.createEvent(Mockito.any(Event.class))).thenReturn(event);

        //System.out.println("************** Event JSON: " + eventJson);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/admin/createEvent")
                .header("Authorization", "Bearer " + "mock-admin-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateEvent_Success() throws Exception {
        // Préparer les données
        Long eventId = 1L;
        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setTitle("France - Italie");
        updatedEvent.setDescription("Match d'ouverture du tournoi de football");
        updatedEvent.setCity(CityEnum.PARIS);
        updatedEvent.setStadium("Stade de France");
        updatedEvent.setSport(SportEnum.FOOTBALL);
        updatedEvent.setCapacity(2000);
        updatedEvent.setPrice(25.0f);

        String eventJson = """
            {
            "eventId": 1,
            "eventTitle": "France - Italie",
            "eventDescription": "Match d'ouverture du tournoi de football",
            "eventCity": "PARIS",
            "eventStadium": "Stade de France",
            "eventDate": "2025-10-16T16:06:37.754",
            "eventCapacity": 2000,
            "eventSport": "FOOTBALL",
            "eventPrice": 25.0
            }
        """;

        // Configurer le comportement du mock
        when(eventService.updateEvent(Mockito.any(Event.class), Mockito.eq(eventId))).thenReturn(updatedEvent);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(put("/admin/updateEvent")
                .header("Authorization", "Bearer " + "mock-admin-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk());
    }
    
}
