package com.pchab.JoParis2024;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.pchab.JoParis2024.controller.EventController;
import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.service.EventService;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class EventControllerUnitTest {
    
    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;
// ----------------------------------------------------------------
    @Test
    public void testEventDetail_Success() throws Exception {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("-100 Kg Homme");
        mockEvent.setDescription("Compétition de judo pour les hommes pesant moins de 100 kg.");
        mockEvent.setCity(CityEnum.PARIS);
        mockEvent.setStadium("Stade de France");
        mockEvent.setDate(LocalDateTime.of(2024, 7, 27, 15, 0));
        mockEvent.setSport(SportEnum.JUDO);
        mockEvent.setCapacity(20000);
        mockEvent.setPrice(50.0f);

        when(eventService.findEventById(1L)).thenReturn((mockEvent));
        Event returnedEvent = eventController.eventDetail(1L);
        assertThat(returnedEvent).isNotNull();
        assertThat(returnedEvent).isEqualTo(mockEvent);
    }

    @Test
    public void testEventDetail_Failure() throws Exception {
        Long eventId = 999L;
        when(eventService.findEventById(eventId)).thenReturn(null);
        Event returnedEvent = eventController.eventDetail(eventId);
        if (returnedEvent == null) {
            assertThat(returnedEvent).isNull();
        } else {
            fail();
        }
    }
// ----------------------------------------------------------------
    @Test
    public void testAllEvents_Success() throws Exception {
        Event mockEvent1 = new Event();
        Event mockEvent2 = new Event();
        List<Event> mockEvents = new ArrayList<>();
        mockEvents.add(mockEvent1);
        mockEvents.add(mockEvent2);
        when(eventService.findAllEvents()).thenReturn(mockEvents);
        List<Event> returnedEvents = eventController.allEvents();
        assertThat(returnedEvents).isNotNull();
        assertThat(returnedEvents).hasSize(2);
        assertThat(returnedEvents).containsExactlyInAnyOrder(mockEvent1, mockEvent2);
        System.out.println("Test terminé avec succès !");
    }

    @Test
    public void testAllEvents_Failure() {
        when(eventService.findAllEvents()).thenReturn(new ArrayList<>());
        List<Event> returnedEvents = eventController.allEvents();
        assertThat(returnedEvents).isNotNull();
        assertThat(returnedEvents).isEmpty();
        System.out.println("Test terminé avec succès !");
    }
// ----------------------------------------------------------------
    @Test
    public void testThreeEvents_Success() throws Exception {
        Event mockEvent1 = new Event();
        Event mockEvent2 = new Event();
        Event mockEvent3 = new Event();
        List<Event> mockEvents = new ArrayList<>();
        mockEvents.add(mockEvent1);
        mockEvents.add(mockEvent2);
        mockEvents.add(mockEvent3);
        when(eventService.findAllEvents()).thenReturn(mockEvents);
        List<Event> returnedEvents = eventController.allEvents();
        assertThat(returnedEvents).isNotNull();
        assertThat(returnedEvents).hasSize(3);
        assertThat(returnedEvents).containsExactlyInAnyOrder(mockEvent1, mockEvent2, mockEvent3);
        System.out.println("Test terminé avec succès !");
    }

    @Test
    public void testThreeEvents_Failure() throws Exception {
        when(eventService.findAllEvents()).thenReturn(null);
        List<Event> returnedEvents = eventController.allEvents();
        assertThat(returnedEvents).isNull();
        System.out.println("Test terminé avec succès !");
    }
// ----------------------------------------------------------------
    @Test
    public void testCreateEvent_Success() throws Exception {
        Event newEvent = new Event();
        newEvent.setTitle("Nouveau Event");
        newEvent.setDescription("Description de l'événement");
        newEvent.setCity(CityEnum.LYON);
        newEvent.setStadium("Nouveau Stade");
        newEvent.setDate(LocalDateTime.of(2024, 8, 15, 18, 0));
        newEvent.setSport(SportEnum.FOOTBALL);
        newEvent.setCapacity(30000);
        newEvent.setPrice(75.0f);

        when(eventService.createEvent(newEvent)).thenReturn(newEvent);
        Event returnedEvent = eventController.createEvent(newEvent);
        assertThat(returnedEvent).isNotNull();
        assertThat(returnedEvent).isEqualTo(newEvent);
        System.out.println("Test terminé avec succès !");
    }

    @Test
    public void testCreateEvent_Failure() throws Exception {
        Event newEvent = new Event();
        newEvent.setTitle("");
        newEvent.setDescription("Description de l'événement");
        newEvent.setCity(CityEnum.LYON);
        newEvent.setStadium("Nouveau Stade");
        newEvent.setDate(LocalDateTime.of(2024, 8, 15, 18, 0));
        newEvent.setSport(SportEnum.FOOTBALL);
        newEvent.setCapacity(30000);
        newEvent.setPrice(75.0f);

        when(eventService.createEvent(newEvent)).thenThrow(new IllegalArgumentException("Title can't be null"));
        
        try {
            eventController.createEvent(newEvent);
            fail("Expected an exception to be thrown due to invalid event data");
        } catch (Exception e) {
            // Exception is expected, test passes
            System.out.println("Test terminé avec succès !");
            assertThat(e).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Title can't be null");
        }
    }
        
// ----------------------------------------------------------------
    @Test
    public void testUpdateEvent_Success() throws Exception {
        Event updateEvent = new Event();
        updateEvent.setId(1L);
        updateEvent.setTitle("Ancien Titre");
        updateEvent.setDescription("Ancienne Description");
        updateEvent.setCity(CityEnum.MARSEILLE);
        updateEvent.setStadium("Ancien Stade");
        updateEvent.setDate(LocalDateTime.of(2024, 9, 10, 20, 0));
        updateEvent.setSport(SportEnum.BASKETBALL);
        updateEvent.setCapacity(15000);
        updateEvent.setPrice(60.0f);

        when(eventService.updateEvent(updateEvent, 1L)).thenReturn(updateEvent);

        ResponseEntity<Event> response = eventController.updateEvent(updateEvent, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        if (response.getBody() != null) {
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getTitle()).isEqualTo("Ancien Titre");
            assertThat(response.getBody().getDescription()).isEqualTo("Ancienne Description");
            assertThat(response.getBody().getCity()).isEqualTo(CityEnum.MARSEILLE);
            assertThat(response.getBody().getStadium()).isEqualTo("Ancien Stade");
            assertThat(response.getBody().getDate()).isEqualTo(LocalDateTime.of(2024, 9, 10, 20, 0));
            assertThat(response.getBody().getSport()).isEqualTo(SportEnum.BASKETBALL);
            assertThat(response.getBody().getCapacity()).isEqualTo(15000);
            assertThat(response.getBody().getPrice()).isEqualTo(60.0f);
        } else {
            fail();
        }

    }

    @Test
    public void testUpdateEvent_Failure() throws Exception {
        Event updateEvent = new Event();
        updateEvent.setId(999L);
        updateEvent.setTitle("Titre Inexistant");
        updateEvent.setDescription("Description Inexistante");
        updateEvent.setCity(CityEnum.NICE);
        updateEvent.setStadium("Stade Inexistant");
        updateEvent.setDate(LocalDateTime.of(2024, 10, 5, 19, 0));
        updateEvent.setSport(SportEnum.JUDO);
        updateEvent.setCapacity(12000);
        updateEvent.setPrice(55.0f);

        when(eventService.updateEvent(updateEvent, 999L)).thenReturn(null);

        ResponseEntity<Event> response = eventController.updateEvent(updateEvent, 999L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ----------------------------------------------------------------
    @Test
    public void testSportList() throws Exception {
        when(eventService.getAllEventSports()).thenReturn(Arrays.asList("FOOTBALL", "BASKETBALL", "JUDO"));

        List<String> sports = eventController.sportList();

        assertThat(sports).isNotNull();
        assertThat(sports).containsExactly("FOOTBALL", "BASKETBALL", "JUDO");
    }

    @Test
    public void testCityList() throws Exception {
        when(eventService.getAllEventsCities()).thenReturn(Arrays.asList("PARIS", "LYON", "MARSEILLE"));

        List<String> cities = eventController.cityList();

        assertThat(cities).isNotNull();
        assertThat(cities).containsExactly("PARIS", "LYON", "MARSEILLE");
    }

}
