package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.NotificationDto;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryNotificationRepository implements NotificationRepository {

    private final Map<UUID, NotificationDto> store = new ConcurrentHashMap<>();

    @Override
    public NotificationDto save(NotificationDto notification) {
        store.put(notification.id(), notification);
        return notification;
    }

    @Override
    public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
        return store.values().stream()
                .filter(notification -> notification.receiverId().equals(receiverId))
                .sorted(Comparator.comparing(NotificationDto::createdAt))
                .toList();
    }

    @Override
    public Optional<NotificationDto> findById(UUID notificationId) {
        return Optional.ofNullable(store.get(notificationId));
    }

    @Override
    public void deleteById(UUID notificationId) {
        store.remove(notificationId);
    }
}
