package com.pchab.JoParis2024.service.impl;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.response.DecodeQRCodeResponse;
import com.pchab.JoParis2024.security.service.SecurityKey;
import com.pchab.JoParis2024.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityKey securityKey;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        System.out.println("TicketServiceImpl - Fetching tickets for userId: " + userId);
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public List<Ticket> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findTicketById(id);
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        String ticketKey = securityKey.generateSecureKey();
        ticket.setTicketKey(ticketKey);
        return ticketRepository.save(ticket);
    }

    @Override
    public BufferedImage generateQRCodeImage(Long ticketId) throws WriterException {
        Ticket ticket = ticketRepository.findTicketById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }

        // Prepare data for QR code
        String ticketKey = ticket.getTicketKey();
        String topText = ticket.getEvent().getTitle();
        String bottomText = ticket.getTicketType();
        Long userId = ticket.getUser().getId();
        String userKey = ticket.getUser().getUserKey();
        Timestamp buyDate = ticket.getBuyDate();

        if (userKey == null || ticketKey == null || buyDate == null) {
            throw new IllegalArgumentException("Invalid ticket data: userKey, ticketKey, or buyDate is null");
        }

        // Generate Secret Key for QR Code
        byte[] secret = ArrayUtils.addAll(userKey.getBytes(), ticketKey.getBytes());

        try {
            // Create HMAC-SHA256 signature
            SecretKeySpec secretKey = new SecretKeySpec(secret, "HmacSHA256");
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            hmacSha256.init(secretKey);

            // Concatenate all input data
            String input = ticketId.toString() + "#" + userId.toString() + "#" + buyDate.toString();
            byte[] signatureH_bytes = hmacSha256.doFinal(input.getBytes());

            // Convert to Base64 String
            String qrCodeData = Base64.getEncoder().encodeToString(signatureH_bytes);

            System.out.println("**** Generating QR Code for Ticket ID: " + ticketId);
            System.out.println("**** User Id = " + userId);
            System.out.println("**** User Key = " + userKey);
            System.out.println("**** Ticket Key = " + ticketKey);
            System.out.println("**** Top Text: " + topText);
            System.out.println("**** Bottom Text: " + bottomText);
            System.out.println("**** QR Code Data: " + qrCodeData);

            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix matrix = barcodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 300, 300);
            return modifiedQRCode(matrix, topText, bottomText);

        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code: " + e.getMessage());
        }
    }

    @Override
    public BufferedImage modifiedQRCode(BitMatrix matrix, String topText, String bottomText) {
        int matrixWidth = matrix.getWidth();
        int matrixHeight = matrix.getHeight();

        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixHeight);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixHeight; j++) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int topTextWidth = fontMetrics.stringWidth(topText);
        int bottomTextWidth = fontMetrics.stringWidth(bottomText);
        int finalWidth = Math.max(matrixWidth, Math.max(topTextWidth, bottomTextWidth)) + 1;
        int finalHeight = matrixHeight + fontMetrics.getHeight() + fontMetrics.getAscent() + 1;

        BufferedImage finalImage = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D finalGraphics = finalImage.createGraphics();
        finalGraphics.setColor(Color.WHITE);
        finalGraphics.fillRect(0, 0, finalWidth, finalHeight);
        finalGraphics.setColor(Color.BLACK);

        finalGraphics.drawImage(image, (finalWidth - matrixWidth) / 2, fontMetrics.getAscent() + 2, null);
        finalGraphics.drawString(topText, (finalWidth - topTextWidth) / 2, fontMetrics.getAscent() + 2);
        finalGraphics.drawString(bottomText, (finalWidth - bottomTextWidth) / 2, finalHeight - fontMetrics.getDescent() - 5);

        return finalImage;
    }

    @Override
    public DecodeQRCodeResponse verifyTicket (String ticketToken) {

        System.out.println("-- TicketServiceImpl - Verifying ticket");

        // Check if the token is valid
        if (ticketToken == null || ticketToken.isEmpty()) {
            throw new IllegalArgumentException("-- TicketServiceImpl - Ticket token is null or empty");
        } else {
            System.out.println("-- TicketServiceImpl - Verifying ticket token");
            if (!jwtUtils.validateJwtToken(ticketToken)) {
                throw new IllegalArgumentException("-- TicketServiceImpl - Invalid ticket token");
            }
        }

        // Check of token exist in database
        Ticket ticket = ticketRepository.findTicketByTicketKey(ticketToken);
        if (ticket == null) {
            throw new IllegalArgumentException("-- TicketServiceImpl - Ticket not found");
        }
        
        // Decode the ticket token
        Map<String, Object> tokenParts = jwtUtils.decodeTicketToken(ticketToken);
        if (tokenParts == null) {
            throw new IllegalArgumentException("-- TicketServiceImpl - Parser - Invalid ticket token");
        }
        
        System.out.println("-- TicketServiceImpl - Decoded ticket token parts: " + tokenParts.toString());

        Long eventId = ((Number) tokenParts.get("eventId")).longValue();
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("-- TicketServiceImpl - Event not found for the ticket");
        }
 
        System.out.println("-- TicketServiceImpl - Event found for the ticket: " + event.getTitle());
        System.out.println("-- TicketServiceImpl - Ticket belongs to: " + tokenParts.get("firstName") + " " + tokenParts.get("lastName"));
        System.out.println("-- TicketServiceImpl - Ticket type: " + tokenParts.get("ticketType"));
        System.out.println("-- TicketServiceImpl - Ticket buy date: " + tokenParts.get("buyDate"));
        System.out.println("-- TicketServiceImpl - Event date: " + event.getDate());
        System.out.println("-- TicketServiceImpl - Event city: " + event.getCity().toString());
        System.out.println("-- TicketServiceImpl - Ticket verification completed successfully");

        // Map the token parts to the response fields
        DecodeQRCodeResponse response = new DecodeQRCodeResponse();
        response.setUserFirstName((String) tokenParts.get("firstName"));
        response.setUserLastName((String) tokenParts.get("lastName"));
        response.setTicketBuyDate(new Timestamp(((Long) tokenParts.get("buyDate"))));
        response.setEventTitle(event.getTitle());
        response.setEventCity((String) event.getCity().toString());
        response.setEventDate(event.getDate());
        response.setTicketType((String) tokenParts.get("ticketType"));

        //System.out.println("Ticket verification successful: " + response.toString());

        return response;
    }
}