package com.pchab.JoParis2024;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pchab.JoParis2024.controller.TicketController;
import com.pchab.JoParis2024.controller.UserController;
import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.security.payload.request.QRCodeRequest;
import com.pchab.JoParis2024.security.payload.response.TicketListResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TicketControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private TicketRepository ticketRepository;
    
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

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
        mockUser.setUserKey("$2a$10$hN8bpMMHRkgoeuIbpRYS8.S2DXY711PPrMQdLxfyReKOiZy0DeIWS");

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

        System.out.println("Created Ticket ID: " + createdTicket.getId());
        System.out.println("Created Ticket User: " + createdTicket.getUser().getId());
        System.out.println("Created Ticket Event: " + createdTicket.getEvent().getId());
        System.out.println("Created Ticket Type: " + createdTicket.getTicketType());
        System.out.println("Created Ticket Buy Date: " + createdTicket.getBuyDate());
        System.out.println("Created Ticket Key: " + createdTicket.getTicketKey());
        


        assertThat(createdTicket).isNotNull();
        assertThat(createdTicket.getId()).isEqualTo(1L);
        assertThat(createdTicket.getUser()).isEqualTo(mockUser);
        assertThat(createdTicket.getEvent()).isEqualTo(mockEvent);
        assertThat(createdTicket.getTicketType()).isEqualTo("Solo");
        assertThat(createdTicket.getBuyDate()).isNotNull();
        //assertThat(createdTicket.getTicketKey()).isNotNull();

        //verify(userService).findUserById(userId);
        //verify(eventService).findEventById(eventId);
        verify(ticketService).createTicket(Mockito.any(Ticket.class));
    }

    @Test
    public void testCreateTicket_Failure() throws Exception {
        Long eventId = 1L;
        Long userId = 2L;

        // Simulation de l'absence d'utilisateur ou d'événement
        when(userService.findUserById(userId)).thenReturn(null); 
        when(eventService.findEventById(eventId)).thenReturn(null); 

        try {
            ticketController.createTicket(userId, eventId, "Solo", new Timestamp(System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid user or event ID");
        }

        verify(userService).findUserById(userId);
        verify(eventService).findEventById(eventId);
        Mockito.verifyNoInteractions(ticketService); // Ensure ticketService was never called

    }

// ----------------------------------------------------------------
    @Test
    public void testListTickets_Success() throws Exception {
        String authorizationHeader = "Bearer some-valid-token";
        Long userId = 2L;

        // Préparation des données
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        Event mockEvent = new Event();
            mockEvent.setId(1L);
            mockEvent.setTitle("-100 Kg Homme");
            mockEvent.setSport(SportEnum.JUDO);
            mockEvent.setDescription("Compétition de judo pour les hommes pesant moins de 100 kg.");
            mockEvent.setCity(CityEnum.PARIS);
            mockEvent.setStadium("Stade de France");
            mockEvent.setDate(LocalDateTime.of(2024, 7, 27, 15, 0));
            mockEvent.setCapacity(20000);
            mockEvent.setPrice(50.0f);

        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockTicket.setUser(mockUser);
        mockTicket.setEvent(mockEvent);
        mockTicket.setTicketType("Solo");

        List<Ticket> mockTickets = new ArrayList<>();
        mockTickets.add(mockTicket);

        TicketListResponse mockTicketResponse = new TicketListResponse();
        mockTicketResponse.setTicketId(1L);
        mockTicketResponse.setTicketType("Solo");

        // Simulation des appels de service
        when(userController.getUserFromToken(authorizationHeader)).thenReturn(ResponseEntity.ok(mockUser));
        when(ticketService.getTicketsByUserId(userId)).thenReturn(mockTickets);

        // Exécution de la requête POST
        mockMvc.perform(post("/tickets/list")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].ticketId").value(1L))
                .andExpect(jsonPath("$[0].ticketType").value("Solo"))
                .andDo(print());
}
    
    @Test
    public void testListTickets_NoUser() throws Exception {
        String authorizationHeader = "Bearer invalid-token";

        // Simulation des appels de service
        when(userController.getUserFromToken(authorizationHeader)).thenReturn(ResponseEntity.status(401).build());

        // Exécution de la requête POST
        mockMvc.perform(post("/tickets/list")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
}

// ----------------------------------------------------------------
    @Test
    public void testGenerateQRCode_Success() throws Exception {
        
        Long ticketId = 1L;
        String authorizationHeader = "Bearer some-valid-token";
        String qrCodeBase64 = "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAIAAAD/gAIDAAAANElEQVR4Xu3BAQ0AAADCoPdPbQ43oAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAfgx1lAABHywbagAAAABJRU5ErkJggg==";

        QRCodeRequest qrCodeRequest = new QRCodeRequest();
        qrCodeRequest.setTicketId(ticketId);

        Ticket mockTicket = new Ticket();
        mockTicket.setId(ticketId);

        // Simulation des appels de service
        when(ticketService.getTicketById(ticketId)).thenReturn(mockTicket);
        when(ticketService.generateQRCodeImage(ticketId)).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        when(ticketService.generateQRCodeImage(ticketId)).thenAnswer(invocation -> {
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setPaint(Color.BLACK);
            graphics.fillRect(0, 0, 100, 100);
            return image;
        });

        // Construction de la requête JSON
        String requestBody = """
            {
                "ticketId": 1
            }
        """;

        // Exécution de la requête POST
        mockMvc.perform(post("/tickets/QRCode")
                .header("Authorization", authorizationHeader) // Ajout du header Authorization
                .contentType(MediaType.APPLICATION_JSON) // Type de contenu JSON
                .content(requestBody)) // Corps de la requête
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200
                .andExpect(jsonPath("$.qrCodeImageBytes").value(qrCodeBase64)) // Vérifie le contenu de la réponse
                .andDo(print()); // Affiche la requête et la réponse dans la console
    }

// ----------------------------------------------------------------
    @Test
    public void testVerifyTicket_Success() throws Exception {

    }

    public void testVerifyTicket_Failure() throws Exception {

    }

}
