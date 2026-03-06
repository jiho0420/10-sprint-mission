package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserUpdateRequest")
public record UpdateUserRequestDto(
        String newUsername,
        String newEmail,
        String newPassword,

        @Schema(hidden = true)
        BinaryContentDto newProfileImage
) {}