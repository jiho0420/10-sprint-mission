package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationRequiredEventListenerTest {

    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationRequiredEventListener listener;

    @Test
    @DisplayName("메시지 이벤트 시 알림 ON 사용자에게만, 발신자는 제외하고 알림을 생성한다.")
    void onMessageCreated_notifiesEnabledReceiversExceptSender() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        ReadStatus senderRs = mock(ReadStatus.class);
        User senderUser = mock(User.class);
        given(senderRs.isNotificationEnabled()).willReturn(true);
        given(senderRs.getUser()).willReturn(senderUser);
        given(senderUser.getId()).willReturn(senderId);

        ReadStatus enabledRs = mock(ReadStatus.class);
        User enabledUser = mock(User.class);
        given(enabledRs.isNotificationEnabled()).willReturn(true);
        given(enabledRs.getUser()).willReturn(enabledUser);
        given(enabledUser.getId()).willReturn(receiverId);

        ReadStatus disabledRs = mock(ReadStatus.class);
        given(disabledRs.isNotificationEnabled()).willReturn(false);

        given(readStatusRepository.findAllByChannelId(channelId))
                .willReturn(List.of(senderRs, enabledRs, disabledRs));

        MessageCreatedEvent event =
                new MessageCreatedEvent(channelId, "general", senderId, "Alice", "hello");

        // when
        listener.on(event);

        // then
        verify(notificationService).create(receiverId, "Alice (#general)", "hello");
        verify(notificationService, times(1)).create(any(), any(), any());
        verify(notificationService, never()).create(eq(senderId), any(), any());
    }

    @Test
    @DisplayName("권한 변경 이벤트 시 당사자에게 이전 -> 이후 권한 알림을 생성한다.")
    void onRoleUpdated_notifiesTarget() {
        // given
        UUID userId = UUID.randomUUID();
        RoleUpdatedEvent event = new RoleUpdatedEvent(userId, Role.USER, Role.CHANNEL_MANAGER);

        // when
        listener.on(event);

        // then
        verify(notificationService).create(userId, "권한이 변경되었습니다.", "USER -> CHANNEL_MANAGER");
    }
}
