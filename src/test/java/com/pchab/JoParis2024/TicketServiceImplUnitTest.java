package com.pchab.JoParis2024;


import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.pchab.JoParis2024.security.service.EncryptionService;
import com.pchab.JoParis2024.security.service.SecurityKey;
import com.pchab.JoParis2024.service.impl.TicketServiceImpl;


@ExtendWith(MockitoExtension.class)
public class TicketServiceImplUnitTest {
    
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private SecurityKey securityKey;

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

        System.out.println("---- Test Create Ticket ----");

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

        when(securityKey.generateSecureKey()).thenReturn("mocked-ticket-key");
        when(ticketRepository.save(newTicket)).thenReturn(savedTicket);
        try {
        when(encryptionService.encrypt("mocked-ticket-key")).thenReturn("mocked-encrypted-key");
        } catch (Exception e) {
            throw new RuntimeException("Error mocking encryptionService.encrypt", e);
        }

        Ticket ticket = ticketServiceImpl.createTicket(newTicket);
        assertEquals(1L, ticket.getId());
        verify(ticketRepository, times(1)).save(newTicket);

        System.out.println("Created Ticket ID: " + ticket.getId());
    } 
    
    @Test
    public void testGenerateQRCodeImage() throws Exception {
        System.out.println("---- Test Generate QR Code Image ----");
        Long ticketId = 1L;

        String mockedTicketKey = "mocked-ticket-key";
        String mockedEncryptedTicketKey = "mocked-encrypted-ticketkey";
        String mockedUserKey = "mocked-userkey";
        String mockedEncryptedUserKey = "mock-encrypted-userkey";

        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("Sample Event");
        mockEvent.setDescription("This is a sample event.");
        mockEvent.setCity(null);
        mockEvent.setSport(null);
        mockEvent.setDate(LocalDateTime.now());

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("P@ssword12456");
        mockUser.setUserKey(mockedEncryptedUserKey);

        Ticket mockTicket = new Ticket();
        mockTicket.setEvent(mockEvent);
        mockTicket.setUser(mockUser);
        mockTicket.setId(ticketId);
        mockTicket.setTicketType("Solo");
        mockTicket.setTicketKey(mockedEncryptedTicketKey);
        mockTicket.setBuyDate(Timestamp.valueOf(LocalDateTime.now()));

        BitMatrix bitMatrix = new BitMatrix(200, 200);

        when(ticketRepository.findTicketById(ticketId)).thenReturn(mockTicket);
        try {
        when(encryptionService.decrypt(mockedEncryptedTicketKey)).thenReturn(mockedTicketKey);
        } catch (Exception e) {
            throw new RuntimeException("Error mocking encryptionService.decrypt", e);
        }
        try {
        when(encryptionService.decrypt(mockedEncryptedUserKey)).thenReturn(mockedUserKey);
        } catch (Exception e) {
            throw new RuntimeException("Error mocking encryptionService.decrypt", e);
        }
        //when(barcodeWriter.encode(anyString(), any(), anyInt(), anyInt())).thenReturn(bitMatrix);

        TicketServiceImpl spyTicketService = org.mockito.Mockito.spy(ticketServiceImpl);
        BufferedImage mockedImage = new BufferedImage(301, 301, BufferedImage.TYPE_INT_RGB);
        doReturn(mockedImage).when(spyTicketService).modifiedQRCode(any(BitMatrix.class), anyString(), anyString());
        
        // Call the method to test
        BufferedImage qrCodeImage = spyTicketService.generateQRCodeImage(ticketId);
        // Assert that the returned image is not null
        assertEquals(301, qrCodeImage.getWidth());
        assertEquals(301, qrCodeImage.getHeight());
        System.out.println("QR Code Image Width: " + qrCodeImage.getWidth());

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

        String mockedTicketKey = "mocked-ticket-key";
        String mockedEncryptedTicketKey = "mocked-encrypted-ticketkey";
        String mockedUserKey = "mocked-userkey";
        String mockedEncryptedUserKey = "mock-encrypted-userkey";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("P@ssword12456");
        mockUser.setUserKey(mockedEncryptedUserKey);

        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("Sample Event");
        mockEvent.setDescription("This is a sample event.");
        mockEvent.setCity(CityEnum.PARIS);
        mockEvent.setSport(SportEnum.FOOTBALL);
        mockEvent.setDate(LocalDateTime.now());

        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockTicket.setEvent(mockEvent);
        mockTicket.setUser(mockUser);
        mockTicket.setTicketType("Solo");
        mockTicket.setTicketKey(mockedEncryptedTicketKey);
        mockTicket.setBuyDate(Timestamp.valueOf(LocalDateTime.now()));

        Map<String, Object> mockDecodedData = Map.of(
            "firstName", "John",
            "lastName", "Doe",
            "buyDate", mockTicket.getBuyDate().getTime(),
            "eventTitle", mockEvent.getTitle(),
            "eventCity", mockEvent.getCity().toString(),
            "eventDate", mockEvent.getDate(),            
            "ticketType", "Solo",
            "ticketId", 1L,
            "eventId", 1L
        );

        // Generate expected signature
        String secretKey = mockedUserKey + mockedTicketKey;
        String input = mockTicket.getId().toString();
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(secretKeySpec);
        byte[] hmacBytes = hmacSha256.doFinal(input.getBytes());
        String expectedSignature = Base64.getEncoder().encodeToString(hmacBytes);

        String qrCode = "1#" + expectedSignature;

        // Mock the dependencies
        when(ticketRepository.findTicketById(1L)).thenReturn(mockTicket);
        try {
        when(encryptionService.decrypt(mockedEncryptedTicketKey)).thenReturn(mockedTicketKey);
        } catch (Exception e) {
            throw new RuntimeException("Error mocking encryptionService.decrypt", e);
        }
        try {
        when(encryptionService.decrypt(mockedEncryptedUserKey)).thenReturn(mockedUserKey);
        } catch (Exception e) {
            throw new RuntimeException("Error mocking encryptionService.decrypt", e);
        }



        DecodeQRCodeResponse response = ticketServiceImpl.verifyTicket(qrCode);
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
        
        assertEquals("John", response.getUserFirstName());
        assertEquals("Doe", response.getUserLastName());
        assertEquals(mockEvent.getTitle(), response.getEventTitle());
        assertEquals(mockEvent.getCity().toString(), response.getEventCity());
        assertEquals(mockEvent.getDate(), response.getEventDate());
        assertEquals(mockTicket.getTicketType(), response.getTicketType());
    }

    @Test
    public void testVerifyTicket_InvalidQRCodeFormat() {
        String invalidQRCode = "invalid_format";
        assertThrows(IllegalArgumentException.class, () -> {
        ticketServiceImpl.verifyTicket(invalidQRCode);
        });
    }

    @Test
    public void testVerifyTicket_TicketNotFound() {
        String qrCode = "999#some_signature"; // Ticket ID 999 does not exist

        when(ticketRepository.findTicketById(999L)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> {
        ticketServiceImpl.verifyTicket(qrCode);
        });
    }

    @Test
    public void testCreateTicket_EncryptionError() {
        Ticket newTicket = new Ticket();
        when(securityKey.generateSecureKey()).thenReturn("mocked-ticket-key");
        try {
        when(encryptionService.encrypt("mocked-ticket-key")).thenThrow(new RuntimeException("Error encrypting ticket key"));
        } catch (Exception e) {
            throw new RuntimeException("Error mocking encryptionService.encrypt", e);
        }
            assertThrows(RuntimeException.class, () -> {
                ticketServiceImpl.createTicket(newTicket);
            });
    }
}
