package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_attachments")
@IdClass(MessageAttachment.class)
@Getter
@NoArgsConstructor
public class MessageAttachment {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "binary_content_id", nullable = false)
    private BinaryContent attachment;

    public MessageAttachment(Message message, BinaryContent attachment) {
        this.message = message;
        this.attachment = attachment;
    }
}
