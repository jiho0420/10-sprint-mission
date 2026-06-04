package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.exception.notification.NotificationForbiddenException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @CacheEvict(value = "notifications", key = "#receiverId")
    public NotificationDto create(UUID receiverId, String title, String content) {
        NotificationDto notification = new NotificationDto(
                UUID.randomUUID(), Instant.now(), receiverId, title, content);
        notificationRepository.save(notification);
        log.info("알림 생성: receiverId={}, title={}", receiverId, title);
        return notification;
    }

    @Override
    @Cacheable(value = "notifications", key = "#receiverId")
    public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
        return notificationRepository.findAllByReceiverId(receiverId);
    }

    @Override
    @CacheEvict(value = "notifications", key = "#requesterId")
    public void delete(UUID notificationId, UUID requesterId) {
        NotificationDto notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        // 본인 알림이 아니면 삭제 불가
        if (!notification.receiverId().equals(requesterId)) {
            throw new NotificationForbiddenException(notificationId, requesterId);
        }
        notificationRepository.deleteById(notificationId);
    }
}
