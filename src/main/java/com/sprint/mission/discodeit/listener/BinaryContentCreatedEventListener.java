package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 바이너리 메타데이터 커밋 이후 실제 바이너리 데이터를 스토리지에 저장하는 리스너.
 *
 * <p>AFTER_COMMIT 시점에 실행되므로, 메타데이터 저장 트랜잭션이 확정된 뒤에만 동작한다.
 * 무거운 파일 I/O를 핵심 트랜잭션에서 분리하는 것이 목적</p>
 *
 * <p>저장 성공/실패에 따른 상태 전이는 {@link BinaryContentService#updateStatus}에 위임한다.
 * AFTER_COMMIT 시점엔 활성 트랜잭션이 없고, 같은 빈 내부 호출은 프록시를 거치지 않아
 * {@code @Transactional(REQUIRES_NEW)}이 무시되기 때문이다.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentCreatedEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @Async("ioTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBinaryContentCreated(BinaryContentCreatedEvent event) {
        try {
            binaryContentStorage.put(event.binaryContentId(), event.bytes());
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.SUCCESS);
            log.info("바이너리 저장 완료: contentId={}", event.binaryContentId());
        } catch (Exception e) {
            log.warn("바이너리 저장 실패: contentId={}", event.binaryContentId(), e);
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.FAIL);
        }
    }
}
