package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode
public class MessageAttachmentId implements Serializable {
    private UUID message;
    private UUID attachment;
}
