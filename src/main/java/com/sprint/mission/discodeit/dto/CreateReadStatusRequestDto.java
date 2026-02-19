package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
@Schema(name = "ReadStatusCreateRequest")
@Getter
@AllArgsConstructor
public class CreateReadStatusRequestDto {
    @NotNull(message = "유저 ID는 필수입니다.")
    private UUID userId;

    @NotNull(message = "채널 ID는 필수입니다.")
    private UUID channelId;

    // 선택적으로 마지막 읽은 시간을 받을 수 있도록
    private Instant lastReadAt;
}
