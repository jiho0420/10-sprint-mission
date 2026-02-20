package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "LoginRequest")
@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDto {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "이름은 필수입니다!")
    private String username;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다!")
    private String password;
}
