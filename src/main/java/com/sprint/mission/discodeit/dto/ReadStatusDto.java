package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "ReadStatus")
@Getter
public class ReadStatusDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;

    public ReadStatusDto(ReadStatus readStatus) {
        this.id = readStatus.getId();
        this.createdAt = readStatus.getCreatedAt();
        this.updatedAt = readStatus.getUpdatedAt();
        this.channelId = readStatus.getChannelId();
        this.userId = readStatus.getUserId();
        this.lastReadAt = readStatus.getLastReadAt();
    }
}
