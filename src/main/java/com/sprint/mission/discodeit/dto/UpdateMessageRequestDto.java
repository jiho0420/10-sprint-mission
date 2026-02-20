package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(name = "MessageUpdateRequest")
@Getter
@AllArgsConstructor
public class UpdateMessageRequestDto {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "수정할 메시지 내용을 입력해주세요.")
    private String newContent;
}
