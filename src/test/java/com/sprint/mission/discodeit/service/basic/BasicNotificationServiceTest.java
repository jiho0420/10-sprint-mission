package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.exception.notification.NotificationForbiddenException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BasicNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private BasicNotificationService notificationService;

    @Test
    @DisplayName("알림을 생성하면 수신자/제목/내용이 담긴 DTO를 저장하고 반환한다.")
    void create_success() {
        // given
        UUID receiverId = UUID.randomUUID();
        given(notificationRepository.save(any(NotificationDto.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        NotificationDto result = notificationService.create(receiverId, "제목", "내용");

        // then
        assertThat(result.receiverId()).isEqualTo(receiverId);
        assertThat(result.title()).isEqualTo("제목");
        assertThat(result.content()).isEqualTo("내용");
        assertThat(result.id()).isNotNull();
        verify(notificationRepository).save(any(NotificationDto.class));
    }

    @Test
    @DisplayName("수신자 ID로 알림 목록을 조회한다.")
    void findAllByReceiverId_success() {
        // given
        UUID receiverId = UUID.randomUUID();
        NotificationDto dto = new NotificationDto(UUID.randomUUID(), Instant.now(), receiverId, "t", "c");
        given(notificationRepository.findAllByReceiverId(receiverId)).willReturn(List.of(dto));

        // when
        List<NotificationDto> result = notificationService.findAllByReceiverId(receiverId);

        // then
        assertThat(result).containsExactly(dto);
    }

    @Test
    @DisplayName("본인 알림은 삭제할 수 있다.")
    void delete_ownNotification_success() {
        // given
        UUID requesterId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        NotificationDto dto = new NotificationDto(notificationId, Instant.now(), requesterId, "t", "c");
        given(notificationRepository.findById(notificationId)).willReturn(Optional.of(dto));

        // when
        notificationService.delete(notificationId, requesterId);

        // then
        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    @DisplayName("타인의 알림 삭제 시 NotificationForbiddenException이 발생한다.")
    void delete_othersNotification_throwsForbidden() {
        // given
        UUID ownerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        NotificationDto dto = new NotificationDto(notificationId, Instant.now(), ownerId, "t", "c");
        given(notificationRepository.findById(notificationId)).willReturn(Optional.of(dto));

        // when & then
        assertThatThrownBy(() -> notificationService.delete(notificationId, requesterId))
                .isInstanceOf(NotificationForbiddenException.class);
        verify(notificationRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("존재하지 않는 알림 삭제 시 NotificationNotFoundException이 발생한다.")
    void delete_missingNotification_throwsNotFound() {
        // given
        UUID notificationId = UUID.randomUUID();
        given(notificationRepository.findById(notificationId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> notificationService.delete(notificationId, UUID.randomUUID()))
                .isInstanceOf(NotificationNotFoundException.class);
    }
}
