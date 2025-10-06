package com.pchab.JoParis2024;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.pchab.JoParis2024.controller.TicketController;
import com.pchab.JoParis2024.controller.UserController;
import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
public class TicketControllerUnitTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private TicketController ticketController;

// -------------------- Test createTicket --------------------------------------------
    @Test
    public void testCreateTicket_Success() throws Exception {
        Long eventId = 1L;
        Long userId = 2L;

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("P@ssword123456");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setRole("USER");
        mockUser.setUserKey("uniqueUserKey123");

        Event mockEvent = new Event();
        mockEvent.setId(eventId);
        mockEvent.setTitle("-100 Kg Homme");
        mockEvent.setSport(SportEnum.JUDO);
        mockEvent.setDescription("Compétition de judo pour les hommes pesant moins de 100 kg.");
        mockEvent.setCity(CityEnum.PARIS);
        mockEvent.setStadium("Stade de France");
        mockEvent.setDate(LocalDateTime.of(2024, 7, 27, 15, 0));
        mockEvent.setCapacity(20000);
        mockEvent.setPrice(50.0f);

        Ticket mockTicket = new Ticket();
        mockTicket.setTicketType("Solo");


        when(userService.findUserById(userId)).thenReturn(mockUser);
        when(eventService.findEventById(eventId)).thenReturn(mockEvent);

        System.out.println("User ID: " + userId);
        System.out.println("Event ID: " + eventId);

        when(ticketService.createTicket(Mockito.any(Ticket.class))).thenAnswer(i -> {
            Ticket t = i.getArgument(0);
            t.setId(1L); // Simulate setting ID after saving
            return t;
        });

        Ticket createdTicket = ticketController.createTicket(userId, eventId, "Solo", new Timestamp(System.currentTimeMillis()));

        System.out.println("***** TEST ******   Created Ticket: " + createdTicket);


        assertThat(createdTicket).isNotNull();
        assertThat(createdTicket.getId()).isEqualTo(1L);
        assertThat(createdTicket.getUser()).isEqualTo(mockUser);
        assertThat(createdTicket.getEvent()).isEqualTo(mockEvent);
        assertThat(createdTicket.getTicketType()).isEqualTo("Solo");
        assertThat(createdTicket.getBuyDate()).isNotNull();
        assertThat(createdTicket.getTicketKey()).isNotNull();

        verify(userService).findUserById(userId);
        verify(eventService).findEventById(eventId);
        verify(ticketService).createTicket(Mockito.any(Ticket.class));
    }

    @Test
    public void testCreateTicket_Failure() throws Exception {

    }

// ----------------------------------------------------------------
    @Test
    public void testListTickets_Success() throws Exception {

    }

    public void testListTickets_Failure() throws Exception {

    }

// ----------------------------------------------------------------
    @Test
    public void testGenerateQRCode_Success() throws Exception {

    }

    public void testGenerateQRCode_Failure() throws Exception {

    }

// ----------------------------------------------------------------
    @Test
    public void testVerifyTicket_Success() throws Exception {

    }

    public void testVerifyTicket_Failure() throws Exception {

    }

}
