package com.pchab.JoParis2024;

import com.pchab.JoParis2024.controller.AuthController;
import com.pchab.JoParis2024.controller.TicketController;
import com.pchab.JoParis2024.controller.UserController;
import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.security.payload.request.QRCodeRequest;
import com.pchab.JoParis2024.security.payload.request.VerifyTicketKeyRequest;
import com.pchab.JoParis2024.security.payload.response.DecodeQRCodeResponse;
import com.pchab.JoParis2024.security.payload.response.QRCodeResponse;
import com.pchab.JoParis2024.security.payload.response.TicketListResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private UserService userService;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserController userController;

    @MockBean
    private AuthController authController;

    @Test
    @WithMockUser(username = "John", roles = {"USER"})
    public void testListTickets_Success() throws Exception {
        // Préparer les données
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        Event event = new Event();
        event.setId(1L);
        event.setTitle("France - Italie");
        event.setDescription("Football Match");
        event.setDate(LocalDateTime.now().plusDays(10));
        event.setCity(CityEnum.PARIS);
        event.setStadium("Stade de France");
        event.setSport(SportEnum.FOOTBALL);
        event.setCapacity(2000);
        event.setPrice(50.0f);


        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setTicketType("Solo");
        ticket.setBuyDate(new Timestamp(System.currentTimeMillis()));

        TicketListResponse ticketResponse = TicketListResponse.fromTicket(ticket);

        // Configurer les mocks
        when(userController.getUserFromToken(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
        when(ticketService.getTicketsByUserId(1L)).thenReturn(List.of(ticket));

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/tickets/list")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketType").value("Solo"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"USER"})
    public void testGenerateQRCode_Success() throws Exception {
        // Préparer les données
        QRCodeRequest qrCodeRequest = new QRCodeRequest();
        qrCodeRequest.setTicketId(1L);

        Ticket ticket = new Ticket();
        ticket.setId(1L);

        QRCodeResponse qrCodeResponse = new QRCodeResponse("Image bytes here");
        qrCodeResponse.setQrCodeImageBytes("mock-base64-string");

        BufferedImage mockImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        // Configurer les mocks
        when(ticketService.getTicketById(1L)).thenReturn(ticket);
        when(ticketService.generateQRCodeImage(1L)).thenReturn(mockImage);

        // JSON de la requête
        String qrCodeJson = """
            {
              "ticketId": 1
            }
        """;

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/tickets/QRCode")
                .header("Authorization", "Bearer mock-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(qrCodeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qrCodeImageBytes").value("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAADElEQVR4XmNgYGAAAAAEAAEP0q3kAAAAAElFTkSuQmCC"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void testVerifyTicket_Success() throws Exception {
        // Préparer les données
        VerifyTicketKeyRequest verifyTicketKeyRequest = new VerifyTicketKeyRequest();
        verifyTicketKeyRequest.setTicketKey("mock-ticket-key");

        DecodeQRCodeResponse decodeResponse = new DecodeQRCodeResponse();
        //decodeResponse.setValid(true);

        // Configurer les mocks
        when(ticketService.verifyTicket("mock-ticket-key")).thenReturn(decodeResponse);

        // JSON de la requête
        String verifyTicketJson = """
            {
              "ticketKey": "mock-ticket-key"
            }
        """;

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/tickets/verify")
                .header("Authorization", "Bearer mock-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(verifyTicketJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testListTickets_Unauthorized() throws Exception {
        // Configurer les mocks
        when(userController.getUserFromToken(Mockito.anyString())).thenReturn(ResponseEntity.badRequest().build());

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/tickets/list")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());      
    }
}