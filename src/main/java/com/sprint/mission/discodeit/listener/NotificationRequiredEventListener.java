package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {

    private final ReadStatusRepository readStatusRepository;
    private final NotificationService notificationService;

    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        readStatusRepository.findAllByChannelId(event.channelId()).stream()
                .filter(ReadStatus::isNotificationEnabled)
                .map(readStatus -> readStatus.getUser().getId())
                // 메시지를 보낸 사람은 알림 대상에서 제외
                .filter(receiverId -> !receiverId.equals(event.senderId()))
                .forEach(receiverId -> notificationService.create(
                        receiverId,
                        event.senderName() + " (#" + event.channelName() + ")",
                        event.content()));
    }

    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {
        notificationService.create(
                event.userId(),
                "권한이 변경되었습니다.",
                event.previousRole() + " -> " + event.newRole());
    }
}
