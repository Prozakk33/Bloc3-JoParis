package com.pchab.JoParis2024;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.service.EventService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    public void testEventDetail_Success() throws Exception {
        // Préparer les données
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);
        event.setTitle("France - Italie");
        event.setDescription("The grand opening of the Olympic Games.");
        event.setCity(CityEnum.PARIS);
        event.setStadium("Stade de France");
        event.setSport(SportEnum.FOOTBALL);
        event.setCapacity(2000);
        event.setPrice(25.0f);

        // Configurer le comportement du mock
        when(eventService.findEventById(eventId)).thenReturn(event);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(get("/event/{id}", eventId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.title").value("France - Italie"))
                .andExpect(jsonPath("$.description").value("The grand opening of the Olympic Games."))
                .andExpect(jsonPath("$.city").value("PARIS"))
                .andExpect(jsonPath("$.sport").value("FOOTBALL"))
                .andExpect(jsonPath("$.capacity").value(2000))
                .andExpect(jsonPath("$.price").value(25.0));

    }

    @Test
    public void testAllEvents() throws Exception {
        // Préparer les données
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");

        List<Event> events = Arrays.asList(event1, event2);

        // Configurer le comportement du mock
        when(eventService.findAllEvents()).thenReturn(events);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(get("/event/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Event 2"));
    }

    @Test
    public void testSportList() throws Exception {
        // Préparer les données
        List<String> sports = Arrays.asList("Football", "Basketball", "Swimming");

        // Configurer le comportement du mock
        when(eventService.getAllEventSports()).thenReturn(sports);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(get("/event/sportList")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("Football"))
                .andExpect(jsonPath("$[1]").value("Basketball"))
                .andExpect(jsonPath("$[2]").value("Swimming"));
    }

    @Test
    public void testCityList() throws Exception {
        // Préparer les données
        List<String> cities = Arrays.asList("Paris", "London", "Tokyo");

        // Configurer le comportement du mock
        when(eventService.getAllEventsCities()).thenReturn(cities);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(get("/event/cityList")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("Paris"))
                .andExpect(jsonPath("$[1]").value("London"))
                .andExpect(jsonPath("$[2]").value("Tokyo"));
    }
}