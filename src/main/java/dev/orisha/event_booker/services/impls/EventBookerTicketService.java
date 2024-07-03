package dev.orisha.event_booker.services.impls;

import dev.orisha.event_booker.data.constants.Type;
import dev.orisha.event_booker.data.models.Event;
import dev.orisha.event_booker.data.models.Ticket;
import dev.orisha.event_booker.data.repositories.TicketRepository;
import dev.orisha.event_booker.dtos.requests.AddTicketRequest;
import dev.orisha.event_booker.dtos.requests.PurchaseTicketRequest;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetAllTicketsResponse;
import dev.orisha.event_booker.dtos.responses.PurchaseTicketResponse;
import dev.orisha.event_booker.exceptions.BadRequestException;
import dev.orisha.event_booker.exceptions.ResourceNotFoundException;
import dev.orisha.event_booker.services.TicketService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static dev.orisha.event_booker.services.utils.ServicesUtils.buildApiResponse;
import static java.math.BigDecimal.ZERO;

@Service
@AllArgsConstructor
public class EventBookerTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    private final ModelMapper mapper;

    @Override
    public Ticket saveTicket(AddTicketRequest request, Event event) {
        Ticket newTicket = mapper.map(request, Ticket.class);
        newTicket.setEvent(event);
        return ticketRepository.save(newTicket);
    }

    @Override
    public ApiResponse<PurchaseTicketResponse> purchaseTicket(PurchaseTicketRequest request) {
        int numberOfTickets = request.getNumberOfTickets();
        if (numberOfTickets <= 0) throw new BadRequestException("Number of Tickets must be greater than 0");
        Ticket ticket = getBy(request.getTicketId());
        Type type = ticket.getType();
        BigDecimal discount = ticket.getDiscount();
        if (type.getMargin() > numberOfTickets) discount = ZERO;
        BigDecimal purchaseAmount = getPurchaseAmount(ticket, numberOfTickets, discount);
        for (int count= 0; count < numberOfTickets; count++) ticket.getBuyerEmails().add(request.getEmail());
        ticketRepository.save(ticket);
        PurchaseTicketResponse response = 
                new PurchaseTicketResponse(numberOfTickets, purchaseAmount,
                        "Ticket purchased successfully");
        return buildApiResponse(response);
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
