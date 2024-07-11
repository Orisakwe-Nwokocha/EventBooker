package dev.orisha.event_booker.services.impls;

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
import dev.orisha.event_booker.exceptions.ResourceNotFoundException;
import dev.orisha.event_booker.services.TicketService;
import dev.orisha.event_booker.services.utils.TicketServiceUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static dev.orisha.event_booker.services.utils.ServicesUtils.buildApiResponse;

@Service
@AllArgsConstructor
public class EventBookerTicketService implements TicketService {

    private final TicketRepository ticketRepository;
    private final ReservedTicketRepository reservedTicketRepository;
    private final ModelMapper mapper;
    private final TicketServiceUtils ticketServiceUtils;

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
        TicketServiceUtils.validateTicketAvailability(quantity, availableTickets);
        LocalDateTime expiryDate = ticket.getEvent().getEventDate().minusDays(5);
        TicketServiceUtils.validateExpiry(expiryDate);
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

    @Override
    public ApiResponse<PurchaseTicketResponse> purchaseTicket(PurchaseTicketRequest request) {
        Ticket ticket = getBy(request.getTicketId());
        int numberOfTickets = request.getNumberOfTickets();
        LocalDateTime eventDate = ticket.getEvent().getEventDate();

        if (ticketServiceUtils.isTicketExpired(eventDate)) {
            ticket = ticketServiceUtils.trackReservedTicketsFor(ticket);
        }

        ticketServiceUtils.validateRequest(request, eventDate, numberOfTickets, ticket);
        ticket = getBy(request.getTicketId());

        BigDecimal purchaseAmount = ticketServiceUtils.calculatePurchaseAmount(ticket, numberOfTickets);
        ticketServiceUtils.updateTicket(ticket, request.getEmail(), numberOfTickets);

        PurchaseTicketResponse response = new PurchaseTicketResponse(numberOfTickets,
                purchaseAmount, "Ticket purchased successfully");
        return buildApiResponse(response);
    }

    @Override
    public Ticket getBy(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
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
