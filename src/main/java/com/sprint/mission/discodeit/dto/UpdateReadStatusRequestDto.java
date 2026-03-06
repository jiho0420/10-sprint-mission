package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "ReadStatusUpdateRequest")
public record UpdateReadStatusRequestDto(
        Instant newLastReadAt
) {
}
