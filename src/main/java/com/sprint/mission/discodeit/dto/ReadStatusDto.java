package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "ReadStatus")
public record ReadStatusDto(
        UUID id,
        UUID channelId,
        UUID userId,
        Instant lastReadAt
) {
}
