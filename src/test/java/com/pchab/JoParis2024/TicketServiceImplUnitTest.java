package com.pchab.JoParis2024;


import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.response.DecodeQRCodeResponse;
import com.pchab.JoParis2024.service.impl.TicketServiceImpl;



@ExtendWith(MockitoExtension.class)
public class TicketServiceImplUnitTest {
    
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private QRCodeWriter barcodeWriter;

    @InjectMocks
    private TicketServiceImpl ticketServiceImpl;

    @Test
    public void testGetTicketsByUserId() {
        Long userId = 1L;
        List<Ticket> mockTickets = List.of(new Ticket(), new Ticket());
        when(ticketRepository.findByUserId(userId)).thenReturn(mockTickets);

        List<Ticket> tickets = ticketServiceImpl.getTicketsByUserId(userId);
        assertEquals(2, tickets.size());
        verify(ticketRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetTicketsByEventId() {
        Long eventId = 1L;
        List<Ticket> mockTickets = List.of(new Ticket(), new Ticket(), new Ticket());
        when(ticketRepository.findByEventId(eventId)).thenReturn(mockTickets);

        List<Ticket> tickets = ticketServiceImpl.getTicketsByEventId(eventId);
        assertEquals(3, tickets.size());
        verify(ticketRepository, times(1)).findByEventId(eventId);
    }

    @Test
    public void testGetTicketById() {
        Long ticketId = 1L;
        Ticket mockTicket = new Ticket();
        when(ticketRepository.findTicketById(ticketId)).thenReturn(mockTicket);

        Ticket ticket = ticketServiceImpl.getTicketById(ticketId);
        assertEquals(mockTicket, ticket);
        verify(ticketRepository, times(1)).findTicketById(ticketId);
    }

    @Test
    public void testCreateTicket() {
        Ticket newTicket = new Ticket();

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Sample Event");
        event.setDescription("This is a sample event.");
        event.setCity(null);
        event.setSport(null);
        event.setDate(LocalDateTime.now());

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("P@ssword12456");

        newTicket.setUser(user);
        newTicket.setEvent(event);
        newTicket.setBuyDate(Timestamp.valueOf(LocalDateTime.now()));

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        when(ticketRepository.save(newTicket)).thenReturn(savedTicket);
        when(jwtUtils.generateTicketKeyToken("John", "Doe", newTicket.getBuyDate(), 1L, newTicket.getTicketType())).thenReturn("mocked-ticket-key");
        
        Ticket ticket = ticketServiceImpl.createTicket(newTicket);
        assertEquals(1L, ticket.getId());
        verify(ticketRepository, times(1)).save(newTicket);
    } 
    
    @Test
    public void testGenerateQRCodeImage() throws Exception {
        Long ticketId = 1L;
        Ticket mockTicket = new Ticket();

        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("Sample Event");
        mockEvent.setDescription("This is a sample event.");
        mockEvent.setCity(null);
        mockEvent.setSport(null);
        mockEvent.setDate(LocalDateTime.now());

        mockTicket.setEvent(mockEvent);
        mockTicket.setId(ticketId);
        mockTicket.setTicketType("Solo");
        mockTicket.setTicketKey("mocked-ticket-key");
        mockTicket.setBuyDate(Timestamp.valueOf(LocalDateTime.now()));

        BitMatrix bitMatrix = new BitMatrix(200, 200);

        when(ticketRepository.findTicketById(ticketId)).thenReturn(mockTicket);

        TicketServiceImpl spyTicketService = org.mockito.Mockito.spy(ticketServiceImpl);
        BufferedImage mockedImage = new BufferedImage(301, 301, BufferedImage.TYPE_INT_RGB);
        doReturn(mockedImage).when(spyTicketService).modifiedQRCode(any(BitMatrix.class), anyString(), anyString());
        
        
        // Call the method to test
        BufferedImage qrCodeImage = spyTicketService.generateQRCodeImage(ticketId);
        // Assert that the returned image is not null
        assertEquals(301, qrCodeImage.getWidth());
        assertEquals(301, qrCodeImage.getHeight());

    }

    @Test
    public void testModifiedQRCode() {
        TicketServiceImpl ticketService = new TicketServiceImpl();
        BitMatrix matrix = new BitMatrix(100, 100);
        String topText = "Top Text";
        String bottomText = "Bottom Text";

        BufferedImage image = ticketService.modifiedQRCode(matrix, topText, bottomText);
        // Assert that the returned image is not null
        assert image != null;
    }

    @Test
    public void  testVerifyTicket() throws Exception {
        String mockTicketToken = "mocked-ticket-key";
        Long ticketId = 1L;
        Ticket mockTicket = new Ticket();

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("P@ssword12456");
       

        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("Sample Event");
        mockEvent.setDescription("This is a sample event.");
        mockEvent.setCity(CityEnum.PARIS);
        mockEvent.setSport(SportEnum.FOOTBALL);
        mockEvent.setDate(LocalDateTime.now());

        mockTicket.setEvent(mockEvent);
        mockTicket.setId(ticketId);
        mockTicket.setUser(mockUser);
        mockTicket.setTicketType("Solo");
        mockTicket.setTicketKey(mockTicketToken);
        mockTicket.setBuyDate(Timestamp.valueOf(LocalDateTime.now()));

        Map<String, Object> mockDecodedData = Map.of(
            "firstName", "John",
            "lastName", "Doe",
            "buyDate", mockTicket.getBuyDate().getTime(),
            "eventTitle", mockEvent.getTitle(),
            "eventCity", mockEvent.getCity().toString(),
            "eventDate", mockEvent.getDate(),            
            "ticketType", "Solo",
            "ticketId", ticketId,
            "eventId", 1L
        );

        //when(ticketRepository.findTicketById(ticketId)).thenReturn(mockTicket);
        when(jwtUtils.validateJwtToken(mockTicketToken)).thenReturn(true);
        when(jwtUtils.decodeTicketToken(mockTicketToken)).thenReturn(mockDecodedData);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(mockEvent));
        when(ticketRepository.findTicketByTicketKey(mockTicketToken)).thenReturn(mockTicket);

        DecodeQRCodeResponse response = ticketServiceImpl.verifyTicket(mockTicketToken);
        System.out.println("---- Test Verify Ticket Response ----");    
        System.out.println(response);
        System.out.println(response.getUserFirstName());
        System.out.println(response.getUserLastName());
        System.out.println(response.getTicketBuyDate());
        System.out.println(response.getEventTitle());
        System.out.println(response.getEventCity());
        System.out.println(response.getEventDate());
        System.out.println(response.getTicketType());
        System.out.println("---- End of Response ----");    

/*      
        DecodeQRCodeResponse response = new DecodeQRCodeResponse();
        response.setUserFirstName("John");
        response.setUserLastName("Doe");
        response.setTicketBuyDate(mockTicket.getBuyDate());
        response.setEventTitle(mockEvent.getTitle());
        response.setEventCity(mockEvent.getCity().toString());
        response.setEventDate(mockEvent.getDate());
        response.setTicketType(mockTicket.getTicketType());
*/
        
        assertEquals("John", response.getUserFirstName());
        assertEquals("Doe", response.getUserLastName());
        assertEquals(mockEvent.getTitle(), response.getEventTitle());
        assertEquals(mockEvent.getCity().toString(), response.getEventCity());
        assertEquals(mockEvent.getDate(), response.getEventDate());
        assertEquals(mockTicket.getTicketType(), response.getTicketType());
    }
}
