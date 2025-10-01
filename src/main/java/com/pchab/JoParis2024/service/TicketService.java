package com.pchab.JoParis2024.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.google.zxing.common.BitMatrix;
import com.pchab.JoParis2024.pojo.Ticket;
import com.stripe.model.tax.Registration;


public interface TicketService {

    List<Ticket> getTicketsByUserId(Long userId);

    List<Ticket> getTicketsByEventId(Long eventId);

    Ticket getTicketById(Long id);

    void createTicket(Ticket ticket);

    BufferedImage generateQRCodeImage(Long ticketId) throws Exception;

    BufferedImage modifiedQRCode(BitMatrix matrix, String topText, String bottomText);
}
