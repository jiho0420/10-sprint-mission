package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
@Schema(name = "MessageCreateRequest")
@Getter
@AllArgsConstructor
public class CreateMessageRequestDto {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "메시지를 입력하세요.")
    private String content;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull(message = "채널 ID는 필수입니다.")
    private UUID channelId;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull(message = "작성자 ID는 필수입니다.")
    private UUID authorId;

    // 파일은 Multipart 파라미터로 받으므로, JSON 스키마에서는 숨김
    @Schema(hidden = true)
    private List<BinaryContentDto> attachments;
}
