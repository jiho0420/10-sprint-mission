package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserUpdateRequest", description = "수정할 User 정보")
public record UpdateUserRequestDto(
        String newUsername,
        String newEmail,
        String newPassword,

        @Schema(hidden = true)
        BinaryContentDto newProfileImage
) {}