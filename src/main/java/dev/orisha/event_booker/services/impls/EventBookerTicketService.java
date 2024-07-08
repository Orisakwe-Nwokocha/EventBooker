package dev.orisha.event_booker.services.impls;

import dev.orisha.event_booker.data.constants.Type;
import dev.orisha.event_booker.data.models.Event;
import dev.orisha.event_booker.data.models.ReservedTicket;
import dev.orisha.event_booker.data.models.Ticket;
import dev.orisha.event_booker.data.repositories.ReservedTicketRepository;
import dev.orisha.event_booker.data.repositories.TicketRepository;
import dev.orisha.event_booker.dtos.requests.AddTicketRequest;
import dev.orisha.event_booker.dtos.requests.PurchaseTicketRequest;
import dev.orisha.event_booker.dtos.requests.ReserveTicketRequest;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetAllTicketsResponse;
import dev.orisha.event_booker.dtos.responses.PurchaseTicketResponse;
import dev.orisha.event_booker.dtos.responses.ReserveTicketResponse;
import dev.orisha.event_booker.exceptions.BadRequestException;
import dev.orisha.event_booker.exceptions.ResourceNotFoundException;
import dev.orisha.event_booker.services.TicketService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static dev.orisha.event_booker.services.utils.ServicesUtils.buildApiResponse;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class EventBookerTicketService implements TicketService {

    private final TicketRepository ticketRepository;
    private final ReservedTicketRepository reservedTicketRepository;

    private final ModelMapper mapper;

    @Override
    public Ticket saveTicket(AddTicketRequest request, Event event) {
        Ticket newTicket = mapper.map(request, Ticket.class);
        newTicket.setEvent(event);
        return ticketRepository.save(newTicket);
    }

    @Override
    @Transactional
    public ApiResponse<ReserveTicketResponse> reserveTicket(ReserveTicketRequest request) {
        Ticket ticket = getBy(request.getTicketId());
        int quantity = request.getQuantity();
        Integer availableTickets = ticket.getAvailableTickets();
        validate(quantity, availableTickets);
        LocalDateTime expiryDate = validateAndGetExpiryDateFrom(ticket.getEvent());
        ReservedTicket reservedTicket = mapper.map(request, ReservedTicket.class);
        reservedTicket.setTicket(ticket);
        reservedTicket = reservedTicketRepository.save(reservedTicket);
        ticket.setAvailableTickets(availableTickets - quantity);
        ticket = ticketRepository.save(ticket);
        return apiResponse(reservedTicket, ticket.getId(), expiryDate);
    }

    private ApiResponse<ReserveTicketResponse> apiResponse(ReservedTicket reservedTicket,
                                                           Long ticketId,
                                                           LocalDateTime expiryDate) {
        ReserveTicketResponse response = mapper.map(reservedTicket, ReserveTicketResponse.class);
        response.setTicketId(ticketId);
        response.setExpiryDate(expiryDate);
        return buildApiResponse(response);
    }

    private static LocalDateTime validateAndGetExpiryDateFrom(Event event) {
        LocalDateTime expiryDate = event.getEventDate().minusDays(5);
        if (now().isAfter(expiryDate)) throw new BadRequestException("Ticket is no longer available");
        return expiryDate;
    }

    private static void validate(int requestedQuantity, Integer availableTickets) {
        if (requestedQuantity <= 0) throw new BadRequestException("Number of tickets must be greater than 0");
        if (requestedQuantity > availableTickets) throw new IllegalStateException("Not enough available tickets");
    }

    @Override
    public ApiResponse<PurchaseTicketResponse> purchaseTicket(PurchaseTicketRequest request) {
        Ticket ticket = getBy(request.getTicketId());
        int numberOfTickets = request.getNumberOfTickets();
        LocalDateTime eventDate = ticket.getEvent().getEventDate();
        boolean isExpired = now().isAfter(eventDate.minusDays(5));
        if (isExpired) ticket = trackReservedTicketsFor(ticket);
        validate(request.getReservedTicketId(), eventDate.minusDays(1), numberOfTickets, ticket);
        Type type = ticket.getType();
        BigDecimal discount = ticket.getDiscount();
        if (type.getMargin() > numberOfTickets || discount == null) discount = ZERO;
        BigDecimal purchaseAmount = getPurchaseAmount(ticket, numberOfTickets, discount);
        for (int count= 0; count < numberOfTickets; count++) ticket.getBuyerEmails().add(request.getEmail());
        ticket.setAvailableTickets(ticket.getAvailableTickets() - numberOfTickets);
        ticketRepository.save(ticket);
        PurchaseTicketResponse response = 
                new PurchaseTicketResponse(numberOfTickets, purchaseAmount,
                        "Ticket purchased successfully");
        return buildApiResponse(response);
    }

    private static void validate(Long reservedTicketId, LocalDateTime expiryDate,
                                 int numberOfTickets, Ticket ticket) {
        if (reservedTicketId == null) {
            if (now().isAfter(expiryDate)) throw new BadRequestException("Ticket is no longer available");
        }
        validate(numberOfTickets, ticket.getAvailableTickets());
    }

    private Ticket trackReservedTicketsFor(Ticket ticket) {
        List<ReservedTicket> reservedTickets =
                reservedTicketRepository.findReservedTicketsFor(ticket.getId());
        for (ReservedTicket reservedTicket : reservedTickets) {
            int quantity = reservedTicket.getQuantity();
            ticket.setAvailableTickets(ticket.getAvailableTickets() + quantity);
        }
        return ticketRepository.save(ticket);
    }

    private static BigDecimal getPurchaseAmount(Ticket ticket, int numberOfTickets, BigDecimal discount) {
        BigDecimal price = ticket.getPrice();
        price = price.multiply(BigDecimal.valueOf(numberOfTickets));
        discount = discount.multiply(BigDecimal.valueOf(numberOfTickets));
        price = price.subtract(discount);
        return price;
    }

    @Override
    public Ticket getBy(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket not found"));
    }

    @Override
    public ApiResponse<List<GetAllTicketsResponse>> getAllTickets() {
        var tickets = ticketRepository.findAll();
        var response = List.of(mapper.map(tickets, GetAllTicketsResponse[].class));
        return buildApiResponse(response);
    }

    @Override
    public ApiResponse<List<GetAllTicketsResponse>> getAllTicketsForEvent(Long eventId) {
        var tickets = ticketRepository.findAllTicketsForEvent(eventId);
        var response = List.of(mapper.map(tickets, GetAllTicketsResponse[].class));
        return buildApiResponse(response);
    }
}
