package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId, // profileImageId

        @JsonProperty("online")
        @Schema(name = "online")
        Boolean online //isOnline
){}

