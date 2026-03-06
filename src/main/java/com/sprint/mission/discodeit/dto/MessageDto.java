package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(name = "Message")
@Getter
public class MessageDto {
    private UUID id;
    private String content;
    private UUID channelId;
    private UUID authorId;
    private List<UUID> attachmentIds;
    private Instant createdAt;
    private Instant updatedAt;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.channelId = message.getChannel() != null ? message.getChannel().getId() : null;
        this.authorId = message.getAuthor() != null ? message.getAuthor().getId() : null;
        this.attachmentIds = message.getAttachments() != null
                ? message.getAttachments().stream().map(BinaryContent::getId).toList()
                : new ArrayList<>();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }
}
