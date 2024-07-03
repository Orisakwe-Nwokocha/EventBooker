package dev.orisha.event_booker.services.impls;

import dev.orisha.event_booker.data.models.Event;
import dev.orisha.event_booker.data.models.Organizer;
import dev.orisha.event_booker.data.models.Ticket;
import dev.orisha.event_booker.data.repositories.OrganizerRepository;
import dev.orisha.event_booker.dtos.requests.AddTicketRequest;
import dev.orisha.event_booker.dtos.requests.CreateEventRequest;
import dev.orisha.event_booker.dtos.requests.RegisterRequest;
import dev.orisha.event_booker.dtos.responses.AddTicketResponse;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.CreateEventResponse;
import dev.orisha.event_booker.dtos.responses.RegisterResponse;
import dev.orisha.event_booker.exceptions.ResourceNotFoundException;
import dev.orisha.event_booker.exceptions.UsernameExistsException;
import dev.orisha.event_booker.services.EventService;
import dev.orisha.event_booker.services.OrganizerService;
import dev.orisha.event_booker.services.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.orisha.event_booker.services.utils.ServicesUtils.buildApiResponse;

@Service
@Slf4j
@AllArgsConstructor
public class EventBookerOrganizerService implements OrganizerService {

    private final OrganizerRepository organizerRepository;
    private final EventService eventService;
    private final TicketService ticketService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {
        log.info("Trying to register new organizer");
        String username = request.getUsername().toLowerCase();
        request.setUsername(username);
        validate(username);
        Organizer newOrganizer = mapper.map(request, Organizer.class);
        newOrganizer.setPassword(passwordEncoder.encode(request.getPassword()));
        newOrganizer = organizerRepository.save(newOrganizer);
        RegisterResponse response = mapper.map(newOrganizer, RegisterResponse.class);
        response.setMessage("Organizer registered successfully");
        log.info("Organizer registered successfully");
        return buildApiResponse(response);
    }

    @Override
    @Transactional
    public ApiResponse<CreateEventResponse> createEvent(CreateEventRequest request) {
        Event event = eventService.saveEvent(request, getBy(request.getOrganizerId()));
        var response = mapper.map(event, CreateEventResponse.class);
        return buildApiResponse(response);
    }

    @Override
    public ApiResponse<AddTicketResponse> addTicketToEvent(AddTicketRequest request) {
        Event event = eventService.getEventBy(request.getEventId());
        Ticket ticket = ticketService.saveTicket(request, event);
        var response = mapper.map(ticket, AddTicketResponse.class);
        response.setEventId(event.getId());
        return buildApiResponse(response);
    }

    @Override
    public ApiResponse<List<String>> getAllGuestsFor(Long eventId) {
        var guests = eventService.getAllGuestsBy(eventId);
        return buildApiResponse(guests);
    }


    private Organizer getBy(Long id) {
        return organizerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Organizer not found"));
    }

    private void validate(String username) {
        boolean usernameExists = organizerRepository.existsByUsername(username);
        if (usernameExists) {
            log.error("Username '{}' is already taken", username);
            throw new UsernameExistsException("Username is already taken");
        }
    }

}
