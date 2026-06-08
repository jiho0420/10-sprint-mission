package com.sprint.mission.discodeit.storage.manager;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentStorageManager {

    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void store(UUID binaryContentId, byte[] bytes) {
        binaryContentStorage.put(binaryContentId, bytes);
        binaryContentService.updateStatus(binaryContentId, BinaryContentStatus.SUCCESS);
        log.info("바이너리 저장 완료: contentId={}", binaryContentId);
    }

    @Recover
    public void recover(RuntimeException e, UUID binaryContentId, byte[] bytes) {
        binaryContentService.updateStatus(binaryContentId, BinaryContentStatus.FAIL);

        String requestId = MDC.get("requestId");
        String errorMessage = e.getMessage();

        log.error("[바이너리 저장 최종 실패] task=binaryContentStore, requestId={}, contentId={}, message={}",
                requestId, binaryContentId, errorMessage);

        notifyAdmins(binaryContentId, requestId, errorMessage);
    }

    private void notifyAdmins(UUID binaryContentId, String requestId, String errorMessage) {
        List<User> admins = userRepository.findAllByRole(Role.ADMIN);

        if (admins.isEmpty()) {
            log.warn("바이너리 저장 실패 알림을 받을 ADMIN 계정이 없습니다. contentId={}", binaryContentId);
            return;
        }

        String content = """
            Task: binaryContentStore
            RequestId: %s
            BinaryContentId: %s
            Error: %s
            """.formatted(requestId, binaryContentId, errorMessage);

        admins.forEach(admin -> notificationService.create(
                admin.getId(),
                "바이너리 저장 실패",
                content
        ));
    }
}
