package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Schema(name = "ReadStatusUpdateRequest")
@AllArgsConstructor
public class UpdateReadStatusRequestDto {
    private Instant newLastReadAt;
}
