package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Schema(name = "ReadStatusUpdateRequest")
@Getter
public class UpdateReadStatusRequestDto {
    private Instant newLastReadAt;
}
