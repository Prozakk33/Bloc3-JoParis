package com.pchab.JoParis2024;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.impl.EventServiceImpl;


@ExtendWith(MockitoExtension.class)
public class EventServiceImplUnitTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketService ticketService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    @Test
    public void testFindEventById() {
        Long eventId = 1L;
        Event mockEvent = new Event();
        mockEvent.setId(eventId);
        mockEvent.setTitle("100m Sprint Final");
        mockEvent.setDescription("The final race of the 100m sprint.");
        mockEvent.setDate(LocalDateTime.of(2024, 7, 26, 18, 0));
        mockEvent.setSport(SportEnum.ATHLETISME);  
        mockEvent.setCity(CityEnum.PARIS);
        mockEvent.setStadium("Stade de France");    
        mockEvent.setCapacity(80000);
        mockEvent.setPrice(150.0f);
        
        when(eventRepository.findById(eventId)).thenReturn(java.util.Optional.of(mockEvent));  
        Event event = eventServiceImpl.findEventById(eventId);  

        assertEquals(mockEvent, event);
    }

    @Test
    public void testUpdateEvent() {
        Long eventId = 1L;
        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setTitle("100m Sprint Final");
        existingEvent.setDescription("The final race of the 100m sprint.");
        existingEvent.setDate(LocalDateTime.of(2024, 7, 26, 18, 0));
        existingEvent.setSport(SportEnum.ATHLETISME); 
        existingEvent.setCity(CityEnum.PARIS);
        existingEvent.setStadium("Stade de France");
        existingEvent.setCapacity(80000);
        existingEvent.setPrice(150.0f);

        Event updatedEvent = new Event();
        updatedEvent.setTitle("100m Sprint Final - Updated");
        updatedEvent.setDescription("Updated description.");
        updatedEvent.setDate(LocalDateTime.of(2024, 7, 26, 18, 0));
        updatedEvent.setSport(SportEnum.ATHLETISME);
        updatedEvent.setCity(CityEnum.PARIS);
        updatedEvent.setStadium("Stade de France");
        updatedEvent.setCapacity(80000);
        updatedEvent.setPrice(150.0f);

        when(eventRepository.findById(eventId)).thenReturn(java.util.Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);

        Event result = eventServiceImpl.updateEvent(updatedEvent, eventId);

        assertEquals(updatedEvent, result);
        assertEquals("100m Sprint Final - Updated", result.getTitle());
    }

    @Test
    public void testCreateEvent() {
        Event newEvent = new Event();
        newEvent.setTitle("Swimming 200m Freestyle");
        newEvent.setDescription("The 200m freestyle swimming event.");
        newEvent.setDate(LocalDateTime.of(2024, 7, 27, 10, 0));
        newEvent.setSport(SportEnum.NATATION);
        newEvent.setCity(CityEnum.PARIS);
        newEvent.setStadium("Stade de France");
        newEvent.setCapacity(80000);
        newEvent.setPrice(150.0f);

        when(eventRepository.save(newEvent)).thenReturn(newEvent);

        Event result = eventServiceImpl.createEvent(newEvent);

        assertEquals(newEvent, result);
        assertEquals("Swimming 200m Freestyle", result.getTitle());
    }

    @Test
    public void testFindAllEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("100m Sprint Final");
        event1.setDescription("The final race of the 100m sprint.");
        event1.setDate(LocalDateTime.of(2024, 7, 26, 18, 0));
        event1.setSport(SportEnum.ATHLETISME);
        event1.setCity(CityEnum.PARIS);
        event1.setStadium("Stade de France");
        event1.setCapacity(80000);
        event1.setPrice(150.0f);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Swimming 200m Freestyle");
        event2.setDescription("The 200m freestyle swimming event.");
        event2.setDate(LocalDateTime.of(2024, 7, 27, 10, 0));
        event2.setSport(SportEnum.NATATION);
        event2.setCity(CityEnum.PARIS);
        event2.setStadium("Stade de France");
        event2.setCapacity(80000);
        event2.setPrice(150.0f);

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> events = eventServiceImpl.findAllEvents();

        assertEquals(2, events.size());
        assertEquals(event1, events.get(0));
        assertEquals(event2, events.get(1));
    }

    @Test
    public void testFindThreeEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("100m Sprint Final");
        event1.setDescription("The final race of the 100m sprint.");
        event1.setDate(LocalDateTime.of(2024, 7, 26, 18, 0));
        event1.setSport(SportEnum.ATHLETISME);
        event1.setCity(CityEnum.PARIS);
        event1.setStadium("Stade de France");
        event1.setCapacity(80000);
        event1.setPrice(150.0f);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Swimming 200m Freestyle");
        event2.setDescription("The 200m freestyle swimming event.");
        event2.setDate(LocalDateTime.of(2024, 7, 27, 10, 0));
        event2.setSport(SportEnum.NATATION);
        event2.setCity(CityEnum.PARIS);
        event2.setStadium("Stade de France");
        event2.setCapacity(80000);
        event2.setPrice(150.0f);

        Event event3 = new Event();
        event3.setId(3L);
        event3.setTitle("Basketball Final");
        event3.setDescription("The final match of the basketball tournament.");
        event3.setDate(LocalDateTime.of(2024, 7, 28, 20, 0));
        event3.setSport(SportEnum.BASKETBALL);
        event3.setCity(CityEnum.PARIS);
        event3.setStadium("Stade de France");
        event3.setCapacity(80000);
        event3.setPrice(150.0f);

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2, event3));

        List<Event> events = eventServiceImpl.findAllEvents();

        assertEquals(3, events.size());
        assertEquals(event1, events.get(0));
        assertEquals(event2, events.get(1));
        assertEquals(event3, events.get(2));
    }

    @Test
    public void testGetAllEventsAdmin() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("100m Sprint Final");
        event1.setDescription("The final race of the 100m sprint.");
        event1.setDate(LocalDateTime.of(2024, 7, 26, 18, 0));
        event1.setSport(SportEnum.ATHLETISME);
        event1.setCity(CityEnum.PARIS);
        event1.setStadium("Stade de France");
        event1.setCapacity(80000);
        event1.setPrice(150.0f);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("P@assword123456");

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setEvent(event1);
        ticket1.setUser(mockUser);
        ticket1.setTicketType("Solo");


        List<Event> eventsList = Arrays.asList(event1);

        List<Ticket> ticketsList = Arrays.asList(ticket1);


        when(eventRepository.findAll()).thenReturn(eventsList);
        when(ticketService.getTicketsByEventId(event1.getId())).thenReturn(ticketsList);


        List<EventAdminResponse> events = eventServiceImpl.getAllEventsAdmin();

        assertEquals(1, events.size());
        //assertEquals(event1.getId(), events.get(0).getId());
        //assertEquals("100m Sprint Final", events.get(0).getTitle());
    }
}