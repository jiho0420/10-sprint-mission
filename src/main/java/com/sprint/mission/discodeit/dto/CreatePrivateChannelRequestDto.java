package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "PrivateChannelCreateRequest")
public record CreatePrivateChannelRequestDto(
        List<UUID> participantIds
) {
}
