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
            "id": 1,
            "title": "France - Italie",
            "description": "Match d'ouverture du tournoi de football",
            "city": "PARIS",
            "stadium": "Stade de France",
            "date": "2025-10-16T16:06:37.754",
            "capacity": 2000,
            "sport": "FOOTBALL",
            "price": 25.0
            }
        """;

        // Configurer le comportement du mock
        when(eventService.createEvent(event)).thenReturn(event);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/admin/createEvent")
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
            "ID": 1,
            "Title": "France - Italie",
            "Description": "Match d'ouverture du tournoi de football",
            "City": "PARIS",
            "Stadium": "Stade de France",
            "Date": "2025-10-16T16:06:37.754",
            "Capacity": 2000,
            "Sport": "FOOTBALL",
            "Price": 25.0
            }
        """;

        // Configurer le comportement du mock
        when(eventService.updateEvent(Mockito.any(Event.class), Mockito.eq(eventId))).thenReturn(updatedEvent);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(put("/admin/updateEvent", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk());
    }
    
}
