package dev.orisha.event_booker.services.utils;

import dev.orisha.event_booker.data.models.ReservedTicket;
import dev.orisha.event_booker.data.models.Ticket;
import dev.orisha.event_booker.data.repositories.ReservedTicketRepository;
import dev.orisha.event_booker.dtos.requests.PurchaseTicketRequest;
import dev.orisha.event_booker.exceptions.BadRequestException;
import dev.orisha.event_booker.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.time.LocalDateTime.now;

@Component
public class TicketServiceUtils {

    private final ReservedTicketRepository reservedTicketRepository;

    public TicketServiceUtils(ReservedTicketRepository reservedTicketRepository) {
        this.reservedTicketRepository = reservedTicketRepository;
    }

    public boolean isTicketExpired(LocalDateTime eventDate) {
        return now().isAfter(eventDate.minusDays(5));
    }

    public void validateRequest(PurchaseTicketRequest request, LocalDateTime eventDate,
                                int numberOfTickets, Ticket ticket) {
        validate(request.getReservedTicketId(), eventDate.minusDays(1), numberOfTickets, ticket);
    }

    public BigDecimal calculatePurchaseAmount(Ticket ticket, int numberOfTickets) {
        BigDecimal discount = ticket.getDiscount();
        if (ticket.getType().getMargin() > numberOfTickets || discount == null) {
            discount = ZERO;
        }
        return getPurchaseAmount(ticket.getPrice(), numberOfTickets, discount);
    }

    public void updateTicket(Ticket ticket, String email, int numberOfTickets) {
        for (int i = 0; i < numberOfTickets; i++) {
            ticket.getBuyerEmails().add(email);
        }
        ticket.setAvailableTickets(ticket.getAvailableTickets() - numberOfTickets);
    }

    public static BigDecimal getPurchaseAmount(BigDecimal price, int numberOfTickets, BigDecimal discount) {
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(numberOfTickets));
        BigDecimal totalDiscount = discount.multiply(BigDecimal.valueOf(numberOfTickets));
        return totalPrice.subtract(totalDiscount);
    }

    public void validate(Long reservedTicketId, LocalDateTime expiryDate, int numberOfTickets, Ticket ticket) {
        int availableTickets = ticket.getAvailableTickets();
        if (reservedTicketId == null) validateExpiry(expiryDate);
        else availableTickets = validateReservedTicket(reservedTicketId, ticket);
        validateTicketAvailability(numberOfTickets, availableTickets);
    }

    public int validateReservedTicket(Long reservedTicketId, Ticket ticket) {
        ReservedTicket reservedTicket = reservedTicketRepository.findById(reservedTicketId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserved ticket invalid or expired"));
        ticket.setAvailableTickets(ticket.getAvailableTickets() + reservedTicket.getQuantity());
        reservedTicketRepository.delete(reservedTicket);
        return ticket.getAvailableTickets();
    }

    public Ticket trackReservedTicketsFor(Ticket ticket) {
        List<ReservedTicket> reservedTickets = reservedTicketRepository.findReservedTicketsFor(ticket.getId());
        for (ReservedTicket reservedTicket : reservedTickets) {
            int quantity = reservedTicket.getQuantity();
            ticket.setAvailableTickets(ticket.getAvailableTickets() + quantity);
            reservedTicketRepository.delete(reservedTicket);
        }
        return ticket;
    }

    public static void validateTicketAvailability(int requestedQuantity, int availableTickets) {
        if (requestedQuantity <= 0) throw new BadRequestException("Number of tickets must be greater than 0");
        if (requestedQuantity > availableTickets) throw new IllegalStateException("Not enough available tickets");
    }

    public static void validateExpiry(LocalDateTime expiryDate) {
        if (now().isAfter(expiryDate)) throw new BadRequestException("Ticket is no longer available");
    }
}
