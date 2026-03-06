package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "Message")
public record MessageDto(
        UUID id,
        String content,
        UUID channelId,
        UserDto author,
        List<BinaryContentDto> attachments,
        Instant createdAt,
        Instant updatedAt
) {
}

