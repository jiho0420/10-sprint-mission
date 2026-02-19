package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant updatedAt;
    private Instant createdAt;
    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt(){
        this.lastActiveAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // 클라이언트가 보낸 특정 시간으로 업데이트하는 메서드
    public void updateLastActiveAt(Instant newTime) {
        this.lastActiveAt = newTime;
        this.updatedAt = Instant.now(); // 상태 변경 시점의 서버 시간을 updatedAt에 기록
    }

    public boolean isOnline(){
        Instant now = Instant.now();
        Duration duration = Duration.between(lastActiveAt, now);
        return (duration.toMinutes() <= 5);
    }

    public void setLastActiveAt(Instant newLastActiveAt) {
    }
}
