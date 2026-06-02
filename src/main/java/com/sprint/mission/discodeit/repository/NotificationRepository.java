package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.NotificationDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    NotificationDto save(NotificationDto notification);
    List<NotificationDto> findAllByReceiverId(UUID receiverId);
    Optional<NotificationDto> findById(UUID notificationId);
    void deleteById(UUID notificationId);
}
