package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Schema(name = "UserStatusUpdateRequest")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatusRequestDto {
    private Instant newLastActiveAt;
}
