package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "UserUpdateRequest")
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequestDto {
    private String newUsername;
    private String newEmail;
    private String newPassword;

    // 파일은 Multipart 파라미터로 받으므로 JSON 스키마에서는 숨김
    @Schema(hidden = true)
    private BinaryContentDto newProfileImage;
}
