package dev.orisha.event_booker.services.utils;

import dev.orisha.event_booker.dtos.responses.ApiResponse;

import static java.time.LocalDateTime.now;

public class ServicesUtils {
    private ServicesUtils() {}

    public static <T> ApiResponse<T> buildApiResponse(T data) {
        return ApiResponse.<T>builder()
                .requestTime(now())
                .success(true)
                .data(data)
                .build();
    }
}
