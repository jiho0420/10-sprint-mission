package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "PublicChannelCreateRequest")
@Getter
@AllArgsConstructor
public class CreatePublicChannelRequestDto {
    @NotBlank(message = "채널 이름은 필수입니다.")
    private String name;
    private String description;
}
