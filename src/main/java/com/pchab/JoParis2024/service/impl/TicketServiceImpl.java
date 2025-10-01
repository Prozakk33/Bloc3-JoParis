package com.pchab.JoParis2024.service.impl;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
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
    public void createTicket(Ticket ticket) {
        //ticket.setUser(userRepository.findById(ticket.getUser().getId()).orElse(null));
        //ticket.setEvent(eventRepository.findById(ticket.getEvent().getId()).orElse(null));*
        System.out.println("Creating ticket for user: " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName());
        System.out.println("Event: " + ticket.getEvent().getTitle());
        String ticketKey = jwtUtils.generateTicketKeyToken(ticket.getUser().getFirstName(), ticket.getUser().getLastName(), ticket.getBuyDate(), ticket.getEvent().getId(), ticket.getTicketType());
        ticket.setTicketKey(ticketKey);

        ticketRepository.save(ticket);
    }

    @Override
    public BufferedImage generateQRCodeImage(Long ticketId) throws WriterException {
        Ticket ticket = ticketRepository.findTicketById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        String ticketToken = ticket.getTicketKey();
        String topText = ticket.getEvent().getTitle();
        String bottomText = ticket.getTicketType();
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix matrix = barcodeWriter.encode(ticketToken, BarcodeFormat.QR_CODE, 300, 300);
        return modifiedQRCode(matrix, topText, bottomText);
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
}