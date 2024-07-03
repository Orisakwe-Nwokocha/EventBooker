package dev.orisha.event_booker.services;

import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetEventResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/db/data.sql")
class EventServiceTest {
    @Autowired
    private EventService eventService;

    @Test
    void getAllEventsFor() {
        ApiResponse<List<GetEventResponse>> response = eventService.getAllEventsFor(100L);
        assertThat(response).isNotNull();
        List<GetEventResponse> data = response.getData();
        System.out.println(data);
        assertThat(data).hasSize(3);

    }
}