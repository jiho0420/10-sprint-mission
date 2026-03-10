package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "PrivateChannelCreateRequest", description = "Private Channel 생성 정보")
public record CreatePrivateChannelRequestDto(
        List<UUID> participantIds
) {
}
