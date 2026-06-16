package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.event.BinaryContentStatusUpdatedEvent;
import com.sprint.mission.discodeit.event.ChannelChangedEvent;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.event.UserChangedEvent;
import com.sprint.mission.discodeit.service.SseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SseRequiredEventListener {

    private final SseService sseService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(NotificationCreatedEvent event) {
        sseService.send(List.of(event.dto().receiverId()), "notifications.created", event.dto());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(BinaryContentStatusUpdatedEvent event) {
        sseService.broadcast("binaryContents.updated", event.dto());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ChannelChangedEvent event) {
        ChannelDto channel = event.dto();
        String eventName = "channels." + event.action();
        if (channel.type() == ChannelType.PUBLIC) {
            sseService.broadcast(eventName, channel);
        } else {
            sseService.send(
                    channel.participants().stream().map(UserDto::id).toList(),
                    eventName, channel);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(UserChangedEvent event) {
        sseService.broadcast("users." + event.action(), event.dto());
    }
}
