package dev.orisha.event_booker.services.impls;

import dev.orisha.event_booker.data.models.Event;
import dev.orisha.event_booker.data.models.Organizer;
import dev.orisha.event_booker.data.repositories.EventRepository;
import dev.orisha.event_booker.dtos.requests.CreateEventRequest;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetEventResponse;
import dev.orisha.event_booker.services.EventService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

import static dev.orisha.event_booker.services.utils.ServicesUtils.buildApiResponse;

@Service
@AllArgsConstructor
public class EventBookerEventService implements EventService {

    private final EventRepository eventRepository;

    private final ModelMapper mapper;

    @Override
    public Event saveEvent(CreateEventRequest request, Organizer organizer) {
        Event newEvent = mapper.map(request, Event.class);
        newEvent.setOrganizer(organizer);
        return eventRepository.save(newEvent);
    }

    @Override
    public Event getEventBy(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(()-> new ResolutionException("Event not found"));
    }

    @Override
    public List<String> getAllGuestsBy(Long id) {
        return getEventBy(id).getGuests();
    }

    @Override
    public ApiResponse<List<GetEventResponse>> getAllEventsFor(Long organizerId) {
        var events = eventRepository.findAllEventsForOrganizer(organizerId);
        var response = List.of(mapper.map(events, GetEventResponse[].class));
        return buildApiResponse(response);
    }
}
