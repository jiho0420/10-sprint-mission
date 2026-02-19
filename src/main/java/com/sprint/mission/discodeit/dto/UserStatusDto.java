package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "UserStatus")
@Getter
public class UserStatusDto {
    private UUID id;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveAt;
    private boolean isOnline;

    public UserStatusDto(UserStatus userStatus) {
        this.id = userStatus.getId();
        this.userId = userStatus.getUserId();
        this.createdAt = userStatus.getCreatedAt();
        this.updatedAt = userStatus.getUpdatedAt();
        this.lastActiveAt = userStatus.getLastActiveAt();
        this.isOnline = userStatus.isOnline();
    }
}
